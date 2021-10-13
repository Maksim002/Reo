package ru.ktsstudio.core_data_measurement_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteTypeWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory

@Dao
interface MeasurementContainerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalContainerWasteTypes(types: List<LocalContainerWasteType>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalWasteCategories(categories: List<LocalWasteCategory>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalSeparateWasteContainers(containers: List<LocalSeparateWasteContainer>): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalMixedContainers(containers: List<LocalMixedWasteContainer>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalContainerTypes(types: List<LocalContainerType>): Completable

    @Query("""UPDATE ${LocalSeparateWasteContainer.TABLE_NAME} 
                    SET ${LocalSeparateWasteContainer.COLUMN_CONTAINER_NAME} = :uniqueName, 
                        ${LocalSeparateWasteContainer.COLUMN_CONTAINER_VOLUME} = :uniqueVolume 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_LOCAL_ID} = :containerId""")
    fun updateSeparateContainers(
        containerId: Long,
        uniqueName: String?,
        uniqueVolume: Float?
    ): Completable

    @Query("DELETE FROM ${LocalContainerType.TABLE_NAME}")
    fun clearContainerTypes(): Completable

    @Query("DELETE FROM ${LocalContainerWasteType.TABLE_NAME}")
    fun clearContainerWasteTypes(): Completable

    @Query("DELETE FROM ${LocalWasteCategory.TABLE_NAME}")
    fun clearWasteCategories(): Completable

    @Query("DELETE FROM ${LocalSeparateWasteContainer.TABLE_NAME}")
    fun clearSeparateWasteContainers(): Completable

    @Query("DELETE FROM ${LocalMixedWasteContainer.TABLE_NAME}")
    fun clearMixedContainers(): Completable

    @Transaction
    @Query("""SELECT * FROM ${LocalContainerWasteType.TABLE_NAME} 
                    WHERE ${LocalContainerWasteType.COLUMN_CONTAINER_LOCAL_ID} = :containerId 
                        AND ${LocalContainerWasteType.COLUMN_DRAFT_STATE} != '"DELETED"'""")
    fun observeContainerWasteTypesBy(containerId: Long): Observable<List<LocalContainerWasteTypeWithRelation>>

    @Transaction
    @Query("""SELECT * FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_LOCAL_ID} = :containerId""")
    fun observeSeparateWasteContainerById(containerId: Long): Observable<LocalSeparateWasteContainerWithRelations>

    @Query("SELECT * FROM ${LocalContainerType.TABLE_NAME}")
    fun getContainerTypes(): Single<List<LocalContainerType>>

    @Query("SELECT * FROM ${LocalWasteCategory.TABLE_NAME}")
    fun getWasteCategories(): Single<List<LocalWasteCategory>>

    @Query("SELECT * FROM ${LocalContainerType.TABLE_NAME} WHERE ${LocalContainerType.COLUMN_ID} = :typeId")
    fun getContainerTypeById(typeId: String): Maybe<LocalContainerType>

    @Query("""SELECT * FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId""")
    fun getSeparateWasteContainersBy(measurementId: Long): Single<List<LocalSeparateWasteContainer>>

    @Transaction
    @Query("""SELECT * FROM ${LocalContainerWasteType.TABLE_NAME} 
                    WHERE ${LocalContainerWasteType.COLUMN_LOCAL_ID} = :wasteTypeId""")
    fun getContainerWasteTypeBy(wasteTypeId: String): Maybe<LocalContainerWasteTypeWithRelation>

    @Transaction
    @Query("""SELECT * FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_LOCAL_ID} = :containerId""")
    fun getSeparateWasteContainerById(containerId: Long): Maybe<LocalSeparateWasteContainerWithRelations>

    @Transaction
    @Query("""SELECT * FROM ${LocalMixedWasteContainer.TABLE_NAME} 
                   WHERE ${LocalMixedWasteContainer.COLUMN_LOCAL_ID} = :containerId""")
    fun getMixedWasteContainerById(containerId: Long): Maybe<LocalMixedWasteContainerWithRelations>

    @Query("""SELECT * FROM ${LocalMixedWasteContainer.TABLE_NAME} 
                    WHERE ${LocalMixedWasteContainer.COLUMN_LOCAL_ID} IN (:containerIds)""")
    fun getMixedWasteContainersByIds(containerIds: List<Long>): Single<List<LocalMixedWasteContainer>>

    @Query("""SELECT * FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_LOCAL_ID} IN (:containerIds)""")
    fun getSeparateContainersByIds(containerIds: List<Long>): Single<List<LocalSeparateWasteContainer>>

    @Query("""DELETE FROM ${LocalMixedWasteContainer.TABLE_NAME} 
                    WHERE ${LocalMixedWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId""")
    fun deleteMixedWasteContainersByMeasurementId(measurementId: Long): Completable

    @Query("""DELETE FROM ${LocalMixedWasteContainer.TABLE_NAME} 
                    WHERE ${LocalMixedWasteContainer.COLUMN_LOCAL_ID} = :containerId""")
    fun deleteMixedWasteContainerById(containerId: Long): Completable

    @Query("DELETE FROM ${LocalSeparateWasteContainer.TABLE_NAME} WHERE ${LocalSeparateWasteContainer.COLUMN_LOCAL_ID} = :containerId")
    fun deleteSeparateContainerById(containerId: Long): Completable

    @Query("""DELETE FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId""")
    fun deleteSeparateWasteContainersByMeasurementId(measurementId: Long): Completable

    @Query("""DELETE FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_LOCAL_ID} IN (:containerIds)""")
    fun deleteSeparateContainersByIds(containerIds: List<Long>): Completable

    @Query("""DELETE FROM ${LocalContainerWasteType.TABLE_NAME} 
                    WHERE ${LocalContainerWasteType.COLUMN_CONTAINER_LOCAL_ID} IN (:containerIds)""")
    fun deleteContainerWasteTypesByContainerIds(containerIds: List<Long>): Completable

    @Query("""DELETE FROM ${LocalContainerWasteType.TABLE_NAME} 
                    WHERE ${LocalContainerWasteType.COLUMN_LOCAL_ID} = :wasteTypeId""")
    fun deleteContainerWasteTypeById(wasteTypeId: String): Completable
}
