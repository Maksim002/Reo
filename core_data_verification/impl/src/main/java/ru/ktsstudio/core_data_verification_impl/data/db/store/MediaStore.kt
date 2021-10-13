package ru.ktsstudio.core_data_verification_impl.data.db.store

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.utils.db.batchedCompletable
import ru.ktsstudio.common.utils.db.batchedObserve
import ru.ktsstudio.core_data_verification_impl.data.db.dao.MediaDao
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 15.12.2020.
 */
class MediaStore @Inject constructor(
    private val mediaDao: MediaDao
) {
    fun saveMedias(medias: List<LocalMedia>): Completable {
        return mediaDao.insert(medias)
    }

    fun deleteMediasByLocalPath(filePaths: List<String>): Completable {
        return batchedCompletable(
            list = filePaths,
            query = mediaDao::deleteMediasByLocalPath
        )
    }

    fun updateMedia(
        mediaLocalId: Long,
        remoteId: String
    ): Completable {
        return mediaDao.updateMedia(mediaLocalId, remoteId)
    }

    fun getAllUnSyncedMedias(): Single<List<LocalMedia>> {
        return mediaDao.getAllUnSyncedMedias()
    }

    fun getAllMedia(): Single<List<LocalMedia>> {
        return mediaDao.getAllMedias()
    }
}