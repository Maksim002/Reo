package ru.ktsstudio.common.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.content.PermissionChecker
import androidx.core.content.getSystemService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.Task
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.utilities.extensions.orFalse
import ru.ktsstudio.utilities.rx.ObservableNonNullValue
import java.util.concurrent.TimeUnit
import javax.inject.Inject

typealias  FusedLocation = android.location.Location

class LocationProviderImpl @Inject constructor(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationProvider {

    private val locationPermissionValue = ObservableNonNullValue(isLocationPermissionGranted)
    private val isLocationPermissionGranted: Boolean
        get() = LOCATION_PERMISSIONS.all { permission ->
            val state = PermissionChecker.checkSelfPermission(context, permission)
            state == PermissionChecker.PERMISSION_GRANTED
        }

    override fun observeLocationPermissionState(): Observable<Boolean> {
        return locationPermissionValue.observable
    }

    override fun observeCurrentLocation(): Observable<Location> {
        return locationPermissionValue.observable
            .switchMap { isLocationPermissionGranted ->
                if (isLocationPermissionGranted) {
                    observeLocationUpdates()
                } else {
                    Observable.just(Location.Unavailable)
                }
            }
    }

    @SuppressLint("MissingPermission")
    override fun getLastLocation(): Maybe<Location> {
        return getLocationInternal { fusedLocationClient.lastLocation }
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Maybe<Location> {
        return getLocationInternal {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
        }
    }

    override fun isGpsEnabled(): Single<Boolean> {
        return Single.fromCallable {
            context.getSystemService<LocationManager>()
                ?.isProviderEnabled(LocationManager.GPS_PROVIDER)
                .orFalse()
        }
    }

    private fun getLocationInternal(fetchFunc: () -> Task<FusedLocation>): Maybe<Location> {
        return Maybe.create<Location> { emitter ->
            if (checkLocationPermissionState().not()) {
                emitter.onSuccess(Location.Unavailable)
                return@create
            }

            fetchFunc().addOnSuccessListener {
                val location = if (it == null) {
                    Location.Unavailable
                } else {
                    Location.Available(it.latitude, it.longitude)
                }
                emitter.onSuccess(location)
            }
                .addOnFailureListener {
                    emitter.tryOnError(it)
                }
                .addOnCanceledListener {
                    emitter.tryOnError(RuntimeException("getLocation canceled"))
                }
        }
            .timeout(LOCATION_TIMEOUT, TimeUnit.SECONDS)
            .onErrorComplete()
    }

    private fun checkLocationPermissionState(): Boolean {
        return isLocationPermissionGranted.also(locationPermissionValue::set)
    }

    @SuppressLint("MissingPermission")
    private fun observeLocationUpdates(): Observable<Location> {
        return Observable.create<Location> { emitter ->
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val location = locationResult
                        ?.lastLocation
                        ?.let { Location.Available(it.latitude, it.longitude) }
                        ?: Location.Unavailable
                    emitter.onNext(location)
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    locationAvailability
                        .isLocationAvailable
                        .takeUnless { it }
                        ?.let { emitter.onNext(Location.Unavailable) }
                }
            }

            fusedLocationClient
                .requestLocationUpdates(
                    LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY),
                    locationCallback,
                    null
                )

            emitter.setCancellable {
                fusedLocationClient
                    .removeLocationUpdates(locationCallback)
            }
        }
    }

    companion object {
        private const val LOCATION_TIMEOUT = 15L
        private val LOCATION_PERMISSIONS = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}