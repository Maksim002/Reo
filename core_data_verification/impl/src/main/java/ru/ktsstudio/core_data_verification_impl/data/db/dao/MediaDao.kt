package ru.ktsstudio.core_data_verification_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference

/**
 * @author Maxim Ovchinnikov on 15.12.2020.
 */
@Dao
interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(medias: List<LocalMedia>): Completable

    @Query("DELETE FROM ${LocalMedia.TABLE_NAME} WHERE ${LocalMedia.COLUMN_FILE_PATH} IN (:filePaths)")
    fun deleteMediasByLocalPath(filePaths: List<String>): Completable

    @Query("UPDATE ${LocalMedia.TABLE_NAME} SET ${LocalMedia.COLUMN_REMOTE_ID} = :remoteId WHERE ${LocalMedia.COLUMN_ID} = :localId")
    fun updateMedia(
        localId: Long,
        remoteId: String
    ): Completable

    @Query("SELECT * FROM ${LocalMedia.TABLE_NAME} WHERE ${LocalMedia.COLUMN_REMOTE_ID} IS null")
    fun getAllUnSyncedMedias(): Single<List<LocalMedia>>

    @Query("SELECT * FROM ${LocalMedia.TABLE_NAME}")
    fun getAllMedias(): Single<List<LocalMedia>>
}