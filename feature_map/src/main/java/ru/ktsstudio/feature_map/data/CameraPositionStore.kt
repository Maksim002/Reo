package ru.ktsstudio.feature_map.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import ru.ktsstudio.feature_map.domain.models.CameraPosition

/**
 * Created by Igor Park on 03/10/2020.
 */
internal interface CameraPositionStore {
    fun saveLastCameraPosition(cameraPosition: CameraPosition): Completable
    fun getLastCameraPosition(): Maybe<CameraPosition>
}