package ru.ktsstudio.core_data_measurement_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurement
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMedia

@Dao
interface MeasurementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeasurements(measurements: List<LocalMeasurement>): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeasurementsMedia(media: List<LocalMeasurementMedia>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeasurementStatuses(statuses: List<LocalMeasurementStatus>): Completable

    @Transaction
    @Query("SELECT * FROM ${LocalMeasurement.TABLE_NAME} WHERE ${LocalMeasurement.COLUMN_LOCAL_ID} = :measurementId")
    fun observeMeasurementWithRelationsById(measurementId: Long): Observable<LocalMeasurementWithRelations>

    @Transaction
    @Query("SELECT * FROM ${LocalMeasurement.TABLE_NAME}")
    fun observeAll(): Observable<List<LocalMeasurementWithRelations>>

    @Transaction
    @Query("SELECT * FROM ${LocalMeasurement.TABLE_NAME} WHERE ${LocalMeasurement.COLUMN_LOCAL_ID} = :id")
    fun observeByLocalId(id: Long): Observable<LocalMeasurementWithRelations>

    @Transaction
    @Query("SELECT * FROM ${LocalMeasurement.TABLE_NAME} WHERE ${LocalMeasurement.COLUMN_MNO_ID} IN (:mnoIds)")
    fun observeByMnoIds(mnoIds: List<String>): Observable<List<LocalMeasurementWithRelations>>

    @Query("""SELECT * FROM ${LocalMeasurement.TABLE_NAME} WHERE ${LocalMeasurement.COLUMN_LOCAL_STATE} != '"SUCCESS"'""")
    fun getNotSyncedMeasurements(): Single<List<LocalMeasurement>>

    @Query(QUERY_MEASUREMENT_MEDIAS)
    fun getMeasurementMedias(measurementId: Long): Single<List<LocalMedia>>

    @Query(QUERY_NOT_SYNCED_MEASUREMENT_MEDIAS)
    fun getNotSyncedMedias(measurementId: Long): Single<List<LocalMedia>>

    @Query("DELETE FROM ${LocalMeasurement.TABLE_NAME}")
    fun clearMeasurements(): Completable

    @Query("DELETE FROM ${LocalMeasurementMedia.TABLE_NAME}")
    fun clearMeasurementMedia(): Completable

    @Query("DELETE FROM ${LocalMeasurementStatus.TABLE_NAME}")
    fun clearMeasurementStatuses(): Completable

    @Query("DELETE FROM ${LocalMeasurement.TABLE_NAME} WHERE ${LocalMeasurement.COLUMN_LOCAL_ID} = :measurementId")
    fun deleteMeasurementById(measurementId: Long): Completable

    @Query("DELETE FROM ${LocalMeasurementMedia.TABLE_NAME} WHERE ${LocalMeasurementMedia.COLUMN_MEASUREMENT_ID} = :measurementId")
    fun deleteMediaByMeasurementId(measurementId: Long): Completable

    @Query("DELETE FROM ${LocalMeasurementMedia.TABLE_NAME} WHERE ${LocalMeasurementMedia.COLUMN_MEDIA_LOCAL_ID} = :mediaLocalId")
    fun deleteMeasurementMediaByMediaId(mediaLocalId: Long): Completable

    @Query("UPDATE ${LocalMeasurement.TABLE_NAME} SET ${LocalMeasurement.COLUMN_COMMENT} = :comment WHERE ${LocalMeasurement.COLUMN_LOCAL_ID} = :measurementId")
    fun updateCommentForMeasurement(measurementId: Long, comment: String): Completable

    @Query("UPDATE ${LocalMeasurement.TABLE_NAME} SET ${LocalMeasurement.COLUMN_GPS_POINT} = :gps WHERE ${LocalMeasurement.COLUMN_LOCAL_ID} = :measurementId")
    fun updateGpsForMeasurement(measurementId: Long, gps: GpsPoint?): Completable

    @Query("UPDATE ${LocalMeasurement.TABLE_NAME} SET ${LocalMeasurement.COLUMN_LOCAL_STATE} = :state WHERE ${LocalMeasurement.COLUMN_LOCAL_ID} = :measurementId")
    fun updateStateForMeasurement(measurementId: Long, state: LocalModelState): Completable

    companion object {
        private const val QUERY_MEASUREMENT_MEDIAS = """
            SELECT m.* FROM ${LocalMeasurementMedia.TABLE_NAME} as mm
            JOIN ${LocalMedia.TABLE_NAME} as m ON mm.${LocalMeasurementMedia.COLUMN_MEDIA_LOCAL_ID} = m.${LocalMedia.COLUMN_LOCAL_ID}
            WHERE mm.${LocalMeasurementMedia.COLUMN_MEASUREMENT_ID} = :measurementId
        """

        private const val QUERY_NOT_SYNCED_MEASUREMENT_MEDIAS = """
            SELECT m.* FROM ${LocalMeasurementMedia.TABLE_NAME} as mm
            JOIN ${LocalMedia.TABLE_NAME} as m ON mm.${LocalMeasurementMedia.COLUMN_MEDIA_LOCAL_ID} = m.${LocalMedia.COLUMN_LOCAL_ID}
            WHERE mm.${LocalMeasurementMedia.COLUMN_MEASUREMENT_ID} = :measurementId 
                AND m.${LocalMedia.COLUMN_LOCAL_STATE} != '"SUCCESS"' 
                OR m.${LocalMedia.COLUMN_REMOTE_ID} == ''
        """
    }
}