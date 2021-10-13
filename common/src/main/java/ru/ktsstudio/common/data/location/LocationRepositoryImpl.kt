package ru.ktsstudio.common.data.location

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.ofType
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import javax.inject.Inject

/**
 * Created by Igor Park on 28/09/2020.
 */
class LocationRepositoryImpl @Inject constructor(
    private val locationProvider: LocationProvider,
    private val schedulers: SchedulerProvider
) : LocationRepository {

    override fun observeLocationPermissionState(): Observable<Boolean> {
        return locationProvider.observeLocationPermissionState()
            .subscribeOn(schedulers.io)
    }

    override fun getLastLocation(): Maybe<GpsPoint> {
        return locationProvider.getLastLocation()
            .getAvailableGps()
            .subscribeOn(schedulers.io)
    }

    override fun getCurrentLocation(): Maybe<GpsPoint> {
        return locationProvider.getCurrentLocation()
            .getAvailableGps()
            .subscribeOn(schedulers.io)
    }

    override fun checkIfGpsEnabled(): Single<Boolean> {
        return locationProvider.isGpsEnabled()
    }

    private fun Maybe<Location>.getAvailableGps(): Maybe<GpsPoint> {
        return ofType<Location.Available>()
            .map { location ->
                GpsPoint(
                    lat = location.latitude,
                    lng = location.longitude
                )
            }
    }
}
