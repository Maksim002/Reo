package ru.ktsstudio.common.data.location

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LocationProvider {
    fun observeLocationPermissionState(): Observable<Boolean>
    fun observeCurrentLocation(): Observable<Location>
    fun getLastLocation(): Maybe<Location>
    fun getCurrentLocation(): Maybe<Location>
    fun isGpsEnabled(): Single<Boolean>
}
