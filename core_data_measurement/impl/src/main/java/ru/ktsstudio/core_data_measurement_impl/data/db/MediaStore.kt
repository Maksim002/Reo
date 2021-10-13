package ru.ktsstudio.core_data_measurement_impl.data.db

import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MediaDao
import javax.inject.Inject

class MediaStore @Inject constructor(private val mediaDao: MediaDao) {
    fun setStateForMedia(
        mediaId: Long,
        state: LocalModelState
    ): Completable = mediaDao.updateSyncState(mediaId, state)
}