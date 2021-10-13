package ru.ktsstudio.core_data_measurement_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMediaPayload

@Dao
interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalMedias(media: List<LocalMedia>): Single<List<Long>>

    @Query("SELECT * FROM ${LocalMedia.TABLE_NAME} WHERE ${LocalMedia.COLUMN_REMOTE_ID} IN (:remoteMediaIds)")
    fun getByRemoteIds(remoteMediaIds: List<String>): Single<List<LocalMedia>>

    @Query("SELECT * FROM ${LocalMedia.TABLE_NAME} WHERE ${LocalMedia.COLUMN_LOCAL_ID} = :mediaLocalId")
    fun getByLocalId(mediaLocalId: Long): Maybe<LocalMedia>

    @Query("DELETE FROM ${LocalMedia.TABLE_NAME}")
    fun clearMedia(): Completable

    @Update(entity = LocalMedia::class)
    fun updateMedia(mediaPayload: LocalMediaPayload): Completable

    @Query("""UPDATE ${LocalMedia.TABLE_NAME} SET ${LocalMedia.COLUMN_REMOTE_ID} = :remoteId, ${LocalMedia.COLUMN_LOCAL_STATE} = '"SUCCESS"' WHERE ${LocalMedia.COLUMN_LOCAL_ID} = :localId""")
    fun updateMediaRemoteId(localId: Long, remoteId: String): Completable

    @Query("UPDATE ${LocalMedia.TABLE_NAME} SET ${LocalMedia.COLUMN_LOCAL_STATE} = :state WHERE ${LocalMedia.COLUMN_LOCAL_ID} = :localId")
    fun updateSyncState(localId: Long, state: LocalModelState): Completable

    @Query("DELETE FROM ${LocalMedia.TABLE_NAME} WHERE ${LocalMedia.COLUMN_LOCAL_ID} = :localId")
    fun deleteMediaById(localId: Long): Completable
}