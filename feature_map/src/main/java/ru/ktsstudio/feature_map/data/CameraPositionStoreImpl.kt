package ru.ktsstudio.feature_map.data

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import ru.ktsstudio.feature_map.domain.models.CameraPosition
import ru.ktsstudio.utilities.extensions.fromJson
import ru.ktsstudio.utilities.extensions.getString
import ru.ktsstudio.utilities.extensions.putString
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Igor Park on 03/10/2020.
 */
internal class CameraPositionStoreImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : CameraPositionStore {
    override fun saveLastCameraPosition(cameraPosition: CameraPosition): Completable {
        return Completable.fromCallable {
            sharedPreferences.putString(LAST_CAMERA_POSITION, gson.toJson(cameraPosition))
        }
    }

    override fun getLastCameraPosition(): Maybe<CameraPosition> {
        return Maybe.create { emitter ->
            sharedPreferences.getString(LAST_CAMERA_POSITION)
                ?.let { gson.fromJson<CameraPosition>(it) }
                ?.let(emitter::onSuccess)
                ?: emitter.onComplete()
        }
    }

    companion object {
        private const val LAST_CAMERA_POSITION = "LAST_CAMERA_POSITION"
    }
}