package ru.ktsstudio.common.data.location

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.data.models.GpsPoint

/**
 * Created by Igor Park on 28/09/2020.
 */
interface LocationRepository {
    fun observeLocationPermissionState(): Observable<Boolean>
    fun getLastLocation(): Maybe<GpsPoint>
    fun getCurrentLocation(): Maybe<GpsPoint>
    fun checkIfGpsEnabled(): Single<Boolean>
}