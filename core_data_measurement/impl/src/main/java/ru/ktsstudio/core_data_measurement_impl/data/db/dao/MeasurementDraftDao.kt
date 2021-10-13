package ru.ktsstudio.core_data_measurement_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphology
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainer

@Dao
interface MeasurementDraftDao {

    @Query("""SELECT * FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalSeparateWasteContainer.COLUMN_DRAFT_STATE} = '"ADDED"'""")
    fun getAddedSeparateWasteContainersBy(measurementId: Long): Single<List<LocalSeparateWasteContainer>>

    @Query("""SELECT * FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                   WHERE ${LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalSeparateWasteContainer.COLUMN_DRAFT_STATE} = '"DELETED"'""")
    fun getDeletedSeparateWasteContainersBy(measurementId: Long): Single<List<LocalSeparateWasteContainer>>

    @Query("""UPDATE ${LocalMixedWasteContainer.TABLE_NAME} 
                    SET ${LocalMixedWasteContainer.COLUMN_DRAFT_STATE} = :draftState 
                    WHERE ${LocalMixedWasteContainer.COLUMN_LOCAL_ID} = :containerId""")
    fun setMixedContainerDraftState(containerId: Long, draftState: DraftState): Completable

    @Query("""UPDATE ${LocalSeparateWasteContainer.TABLE_NAME} 
                    SET ${LocalSeparateWasteContainer.COLUMN_DRAFT_STATE} = :draftState 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_LOCAL_ID} = :containerId""")
    fun setSeparateContainerDraftState(containerId: Long, draftState: DraftState): Completable

    @Query("""UPDATE ${LocalContainerWasteType.TABLE_NAME} 
                    SET ${LocalContainerWasteType.COLUMN_DRAFT_STATE} = :draftState 
                    WHERE ${LocalContainerWasteType.COLUMN_LOCAL_ID} = :wasteTypeId""")
    fun setContainerWasteTypeDraftState(wasteTypeId: String, draftState: DraftState): Completable

    @Query("""UPDATE ${LocalMedia.TABLE_NAME} 
                    SET ${LocalMedia.COLUMN_DRAFT_STATE} = :draftState 
                    WHERE ${LocalMedia.COLUMN_LOCAL_ID} = :mediaId""")
    fun setMediaDraftState(mediaId: Long, draftState: DraftState): Completable

    @Query("""UPDATE ${LocalMorphology.TABLE_NAME} 
                    SET ${LocalMorphology.COLUMN_DRAFT_STATE} = :draftState 
                    WHERE ${LocalMorphology.COLUMN_LOCAL_ID} = :morphologyItemId""")
    fun setMorphologyItemDraftState(morphologyItemId: Long, draftState: DraftState): Completable

    @Query("""DELETE FROM ${LocalMixedWasteContainer.TABLE_NAME}
                    WHERE ${LocalMixedWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                    AND ${LocalMixedWasteContainer.COLUMN_DRAFT_STATE} = '"ADDED"'""")
    fun removeAddedMixedContainersFor(measurementId: Long): Completable

    @Query("""UPDATE ${LocalMixedWasteContainer.TABLE_NAME} 
                    SET ${LocalMixedWasteContainer.COLUMN_DRAFT_STATE} = '"IDLE"' 
                    WHERE ${LocalMixedWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalMixedWasteContainer.COLUMN_DRAFT_STATE} = '"DELETED"'""")
    fun rollbackDeletedMixedContainersFor(measurementId: Long): Completable

    @Query("""DELETE FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalSeparateWasteContainer.COLUMN_DRAFT_STATE} = '"ADDED"'""")
    fun removeAddedSeparateContainersFor(measurementId: Long): Completable

    @Query("""UPDATE ${LocalSeparateWasteContainer.TABLE_NAME} 
                    SET ${LocalSeparateWasteContainer.COLUMN_DRAFT_STATE} = '"IDLE"' 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalSeparateWasteContainer.COLUMN_DRAFT_STATE} = '"DELETED"'""")
    fun rollbackDeletedSeparateContainersFor(measurementId: Long): Completable

    @Query("""DELETE FROM ${LocalContainerWasteType.TABLE_NAME} 
                    WHERE ${LocalContainerWasteType.COLUMN_CONTAINER_LOCAL_ID} IN (:containerIds) 
                        AND ${LocalContainerWasteType.COLUMN_DRAFT_STATE} = '"ADDED"'""")
    fun removeAddedWasteTypesFor(containerIds: List<Long>): Completable

    @Query("""UPDATE ${LocalContainerWasteType.TABLE_NAME} 
                    SET ${LocalContainerWasteType.COLUMN_DRAFT_STATE} = '"IDLE"' 
                    WHERE ${LocalContainerWasteType.COLUMN_CONTAINER_LOCAL_ID} IN (:containerIds) 
                        AND ${LocalContainerWasteType.COLUMN_DRAFT_STATE} = '"DELETED"'""")
    fun rollbackDeletedWasteTypesFor(containerIds: List<Long>): Completable

    @Query("""DELETE FROM ${LocalMorphology.TABLE_NAME} 
                    WHERE ${LocalMorphology.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalMorphology.COLUMN_DRAFT_STATE} = '"ADDED"'""")
    fun removeAddedMorphologyItemsFor(measurementId: Long): Completable

    @Query("""UPDATE ${LocalMorphology.TABLE_NAME} 
                    SET ${LocalMorphology.COLUMN_DRAFT_STATE} = '"IDLE"' 
                    WHERE ${LocalMorphology.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalContainerWasteType.COLUMN_DRAFT_STATE} = '"DELETED"'""")
    fun rollbackDeletedMorphologyItemsFor(measurementId: Long): Completable

    @Query("""DELETE FROM ${LocalMixedWasteContainer.TABLE_NAME} 
                    WHERE ${LocalMixedWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalMixedWasteContainer.COLUMN_DRAFT_STATE} = '"DELETED"'""")
    fun removeDeletedMixedContainersFor(measurementId: Long): Completable

    @Query("""UPDATE ${LocalMixedWasteContainer.TABLE_NAME} 
                    SET ${LocalMixedWasteContainer.COLUMN_DRAFT_STATE} = '"IDLE"' 
                    WHERE ${LocalMixedWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalMixedWasteContainer.COLUMN_DRAFT_STATE} = '"ADDED"'""")
    fun approveAddedMixedContainersFor(measurementId: Long): Completable

    @Query("""DELETE FROM ${LocalSeparateWasteContainer.TABLE_NAME} 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId
                        AND ${LocalSeparateWasteContainer.COLUMN_DRAFT_STATE} = '"DELETED"'""")
    fun removeDeletedSeparateContainersFor(measurementId: Long): Completable

    @Query("""UPDATE ${LocalSeparateWasteContainer.TABLE_NAME} 
                    SET ${LocalSeparateWasteContainer.COLUMN_DRAFT_STATE} = '"IDLE"' 
                    WHERE ${LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                    AND ${LocalSeparateWasteContainer.COLUMN_DRAFT_STATE} = '"ADDED"'""")
    fun approveAddedSeparateContainersFor(measurementId: Long): Completable

    @Query("""DELETE FROM ${LocalContainerWasteType.TABLE_NAME} 
                    WHERE ${LocalContainerWasteType.COLUMN_CONTAINER_LOCAL_ID} IN (:containerIds) 
                        AND ${LocalContainerWasteType.COLUMN_DRAFT_STATE} = '"DELETED"'""")
    fun removeDeletedWasteTypesFor(containerIds: List<Long>): Completable

    @Query("""UPDATE ${LocalContainerWasteType.TABLE_NAME} 
                    SET ${LocalContainerWasteType.COLUMN_DRAFT_STATE} = '"IDLE"' 
                    WHERE ${LocalContainerWasteType.COLUMN_CONTAINER_LOCAL_ID} IN (:containerIds) 
                        AND ${LocalContainerWasteType.COLUMN_DRAFT_STATE} = '"ADDED"'""")
    fun approveAddedWasteTypesFor(containerIds: List<Long>): Completable

    @Query("""DELETE FROM ${LocalMorphology.TABLE_NAME} 
                    WHERE ${LocalMorphology.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId
                        AND ${LocalMorphology.COLUMN_DRAFT_STATE} = '"DELETED"'""")
    fun removeDeletedMorphologyItemsFor(measurementId: Long): Completable

    @Query("""UPDATE ${LocalMorphology.TABLE_NAME} 
                    SET ${LocalMorphology.COLUMN_DRAFT_STATE} = '"IDLE"' 
                    WHERE ${LocalMorphology.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId 
                        AND ${LocalContainerWasteType.COLUMN_DRAFT_STATE} = '"ADDED"'""")
    fun approveAddedMorphologyItemsFor(measurementId: Long): Completable
}
