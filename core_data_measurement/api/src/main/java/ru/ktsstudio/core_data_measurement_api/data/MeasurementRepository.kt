package ru.ktsstudio.core_data_measurement_api.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Morphology
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategory
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementComposite
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import ru.ktsstudio.core_data_measurement_api.domain.WasteGroupComposite

interface MeasurementRepository {
    fun syncedMeasurements(): Completable
    fun getContainerTypes(): Single<List<ContainerType>>
    fun getContainerTypeById(id: String): Maybe<ContainerType>
    fun getMorphologyById(categoryId: Long): Maybe<MorphologyItem>
    fun getCompositeWasteGroups(): Single<List<WasteGroupComposite>>
    fun getMixedWasteContainerById(id: Long): Maybe<MixedWasteContainerComposite>
    fun getSeparateWasteContainerById(containerLocalId: Long): Maybe<SeparateContainerComposite>
    fun getWasteCategories(): Single<List<WasteCategory>>

    fun createAndObserveMeasurementComposite(
        mnoId: String,
        gpsPoint: GpsPoint?,
        date: Instant
    ): Observable<MeasurementComposite>

    fun createMediaForMeasurement(
        measurementId: Long,
        category: MeasurementMediaCategory
    ): Single<Long>

    fun createSeparateWasteContainer(
        measurementId: Long,
        containerTypeId: String,
        mnoContainerId: String?,
        isUnique: Boolean,
        draftState: DraftState
    ): Maybe<SeparateContainerComposite>

    fun fetchMeasurements(): Single<List<Measurement>>

    fun updateMorphology(
        measurementId: Long,
        morphology: Morphology
    ): Completable

    fun uploadMeasurement(measurementId: Long): Completable
    fun updateLocationForMeasurement(measurementId: Long, gpsPoint: GpsPoint?): Completable
    fun updateMedia(
        mediaLocalId: Long,
        location: GpsPoint?,
        filePath: String,
        date: Instant
    ): Completable

    fun updateMeasurement(
        measurementId: Long,
        separateContainers: List<SeparateContainerComposite>,
        mixedContainers: List<MixedWasteContainerComposite>,
        morphologyItems: List<MorphologyItem>,
        comment: String
    ): Completable

    fun updateSeparateContainers(containers: List<SeparateContainerComposite>): Completable
    fun setMeasurementImpossible(
        mnoId: String,
        impossibilityReason: String,
        gpsPoint: GpsPoint?,
        date: Instant
    ): Completable

    fun saveMixedContainer(
        measurementId: Long,
        container: MixedWasteContainerComposite
    ): Completable

    fun updateMixedContainer(container: MixedWasteContainerComposite): Completable

    fun observeContainerWasteTypesByContainerId(
        containerLocalId: Long
    ): Observable<List<ContainerWasteType>>

    fun observeMorphologiesByMeasurementId(measurementId: Long): Observable<List<MorphologyItem>>
    fun observeMeasurementComposite(measurementLocalId: Long): Observable<MeasurementComposite>
    fun observeMeasurementList(): Observable<List<Measurement>>
    fun observeMeasurementsByMnoIds(mnoIds: List<String>): Observable<List<Measurement>>
    fun observeMeasurementByLocalId(id: Long): Observable<Measurement>

    fun checkMeasurementForChanges(measurementId: Long): Single<Boolean>
    fun rollbackChangedMeasurementEntities(measurementId: Long): Completable
    fun rollbackChangedSeparateContainer(containerId: Long): Completable
    fun approveMeasurementChanges(measurementId: Long): Completable

    fun deleteMeasurementById(measurementId: Long): Completable
    fun deleteMediaWithRelations(mediaLocalId: Long): Completable
    fun deleteSeparateWasteContainer(containerLocalId: Long): Completable
    fun deleteMorphologyById(categoryId: Long): Completable

    fun setMixedContainerDeleted(containerId: Long): Completable
    fun setSeparateContainerDeleted(containerId: Long): Completable
    fun setMediaDeleted(mediaId: Long): Completable
    fun setWasteTypeDeleted(wasteTypeId: String): Completable
    fun setMorphologyItemDeleted(morphologyItemId: Long): Completable

    companion object {
        const val REVISION_STATUS_ID = "RevisionRequested"
        const val MODERATION_STATUS_ID = "Moderation"
    }
}
