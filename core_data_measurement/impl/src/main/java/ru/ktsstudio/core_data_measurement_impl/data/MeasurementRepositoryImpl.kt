package ru.ktsstudio.core_data_measurement_impl.data

import android.content.Context
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.threeten.bp.Instant
import org.threeten.bp.Month
import org.threeten.bp.ZoneId
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.common.domain.models.Season
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.alsoCompletable
import ru.ktsstudio.common.utils.rx.onErrorCompletableAndConsumeNetworkUnavailable
import ru.ktsstudio.common.utils.toFormData
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository.Companion.MODERATION_STATUS_ID
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
import ru.ktsstudio.core_data_measurement_impl.data.db.MeasurementDb
import ru.ktsstudio.core_data_measurement_impl.data.db.MeasurementDraftStore
import ru.ktsstudio.core_data_measurement_impl.data.db.MeasurementStore
import ru.ktsstudio.core_data_measurement_impl.data.db.MediaStore
import ru.ktsstudio.core_data_measurement_impl.data.db.MnoStore
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteTypeWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurement
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphology
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphologyWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroupWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.network.MeasurementNetworkApi
import ru.ktsstudio.core_data_measurement_impl.data.network.MediaApi
import ru.ktsstudio.core_data_measurement_impl.data.network.model.MeasurementToSend
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteMeasurement
import java.io.File
import javax.inject.Inject

typealias MeasurementLocalId = Long
typealias SeparateWasteContainerLocalId = Long

class MeasurementRepositoryImpl @Inject constructor(
    private val measurementNetworkApi: MeasurementNetworkApi,
    private val mediaApi: MediaApi,
    private val mnoStore: MnoStore,
    private val mediaStore: MediaStore,
    private val measurementStore: MeasurementStore,
    private val measurementDraftStore: MeasurementDraftStore,
    private val morphologyStore: MorphologyStore,
    private val measurementNetworkMapper: Mapper<RemoteMeasurement, Measurement>,
    private val containerTypeMapper: Mapper<LocalContainerType, ContainerType>,
    private val measurementWithRelationsMapper: Mapper<LocalMeasurementWithRelations, Measurement>,
    private val measurementToSendMapper: Mapper<LocalMeasurementWithRelations, MeasurementToSend>,
    private val measurementCompositeMapper: Mapper<LocalMeasurementWithRelations, MeasurementComposite>,
    private val mixedWasteContainerCompositeMapper: Mapper<LocalMixedWasteContainerWithRelations, MixedWasteContainerComposite>,
    private val mixedWasteContainerCompositeDbMapper: Mapper2<MixedWasteContainerComposite, Long, LocalMixedWasteContainer>,
    private val mixedWasteContainerCompositeDbMerger: Mapper2<MixedWasteContainerComposite, LocalMixedWasteContainer, LocalMixedWasteContainer>,
    private val separateWasteContainerDbMerger: Mapper2<SeparateContainerComposite, LocalSeparateWasteContainer, LocalSeparateWasteContainer>,
    private val separateContainerCompositeMapper: Mapper<LocalSeparateWasteContainerWithRelations, SeparateContainerComposite>,
    private val wasteCategoriesMapper: Mapper<LocalWasteCategory, WasteCategory>,
    private val containerWasteTypeMapper: Mapper<LocalContainerWasteTypeWithRelation, ContainerWasteType>,
    private val containerWasteTypeDbMapper: Mapper2<ContainerWasteType, Long, LocalContainerWasteType>,
    private val containerWasteTypeDbMerger: Mapper2<ContainerWasteType, LocalContainerWasteType, LocalContainerWasteType>,
    private val measurementSaveDelegate: MeasurementSaveDelegate,
    private val morphologyMapper: Mapper<LocalMorphologyWithRelation, MorphologyItem>,
    private val morphologyDbMapper: Mapper2<MorphologyItem, Long, LocalMorphology>,
    private val wasteGroupCompositeMapper: Mapper<LocalWasteGroupWithRelations, WasteGroupComposite>,
    private val schedulers: SchedulerProvider,
    private val context: Context,
    private val resources: ResourceManager,
    private val dataBase: MeasurementDb
) : MeasurementRepository {
    override fun syncedMeasurements(): Completable {
        return measurementStore.getNotSyncedMeasurementIds()
            .flatMapCompletable { measurementsToSync ->
                Completable.merge(
                    measurementsToSync.map { measurementId ->
                        rollbackChangedMeasurementEntities(measurementId)
                            .andThen(uploadMeasurement(measurementId))
                    }
                )
            }
            .subscribeOn(schedulers.io)
    }

    override fun getContainerTypes(): Single<List<ContainerType>> {
        return measurementStore.getContainerTypes()
            .map(containerTypeMapper::map)
            .subscribeOn(schedulers.io)
    }

    override fun getContainerTypeById(id: String): Maybe<ContainerType> {
        return measurementStore.getContainerTypeById(id)
            .map(containerTypeMapper::map)
            .subscribeOn(schedulers.io)
    }

    override fun getMorphologyById(categoryId: Long): Maybe<MorphologyItem> {
        return morphologyStore.getMorphologyById(categoryId)
            .map(morphologyMapper::map)
            .subscribeOn(schedulers.io)
    }

    override fun getCompositeWasteGroups(): Single<List<WasteGroupComposite>> {
        return morphologyStore.getCompositeWasteGroups()
            .map(wasteGroupCompositeMapper::map)
            .subscribeOn(schedulers.io)
    }

    override fun getMixedWasteContainerById(id: Long): Maybe<MixedWasteContainerComposite> {
        return measurementStore.getMixedWasteContainerById(id)
            .map(mixedWasteContainerCompositeMapper::map)
            .subscribeOn(schedulers.io)
    }

    override fun getSeparateWasteContainerById(containerLocalId: Long): Maybe<SeparateContainerComposite> {
        return measurementStore.getSeparateWasteContainerById(containerLocalId)
            .map(separateContainerCompositeMapper::map)
            .subscribeOn(schedulers.io)
    }

    override fun getWasteCategories(): Single<List<WasteCategory>> {
        return measurementStore.getWasteCategories()
            .map(wasteCategoriesMapper::map)
            .subscribeOn(schedulers.io)
    }

    override fun createAndObserveMeasurementComposite(
        mnoId: String,
        gpsPoint: GpsPoint?,
        date: Instant
    ): Observable<MeasurementComposite> {
        val emptyMeasurement = LocalMeasurement(
            remoteId = null,
            mnoId = mnoId,
            measurementStatusId = MODERATION_STATUS_ID,
            gpsPoint = gpsPoint,
            season = getSeasonString(date.month()),
            date = date,
            isPossible = true,
            impossibilityReason = null,
            comment = null,
            revisionComment = null,
            state = LocalModelState.SUCCESS
        )
        return measurementStore.saveMeasurements(listOf(emptyMeasurement))
            .map { it.first() }
            .flatMapObservable(measurementStore::observeMeasurementById)
            .map(measurementCompositeMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }

    override fun createMediaForMeasurement(
        measurementId: Long,
        category: MeasurementMediaCategory
    ): Single<Long> {
        return measurementStore.createMediaForMeasurement(measurementId, category)
            .subscribeOn(schedulers.io)
    }

    override fun createSeparateWasteContainer(
        measurementId: Long,
        containerTypeId: String,
        mnoContainerId: String?,
        isUnique: Boolean,
        draftState: DraftState
    ): Maybe<SeparateContainerComposite> {
        return measurementStore.saveSeparateContainers(
            listOf(
                LocalSeparateWasteContainer(
                    measurementLocalId = measurementId,
                    mnoContainerId = mnoContainerId.takeIf { isUnique.not() },
                    mnoUniqueContainerId = mnoContainerId.takeIf { isUnique },
                    containerTypeId = containerTypeId,
                    draftState = draftState,
                    isUnique = isUnique,
                    containerName = null,
                    containerVolume = null
                )
            )
        )
            .map { it.first() }
            .flatMapMaybe { createdContainer ->
                measurementStore.getSeparateWasteContainerById(createdContainer)
                    .map(separateContainerCompositeMapper::map)
            }
            .subscribeOn(schedulers.io)
    }

    override fun fetchMeasurements(): Single<List<Measurement>> {
        return measurementNetworkApi.getMeasurements()
            .map(measurementNetworkMapper::map)
            .subscribeOn(schedulers.io)
    }

    override fun uploadMeasurement(measurementId: Long): Completable {
        return measurementStore.setStateForMeasurement(
                measurementId,
                LocalModelState.PENDING
            )
            .andThen(uploadMeasurementMedia(measurementId))
            .andThen(
                measurementStore.observeMeasurementById(measurementId)
                    .firstOrError()
                    .flatMapCompletable {
                        measurementNetworkApi.sendMeasurement(measurementToSendMapper.map(it))
                            .flatMapCompletable { measurement ->
                                replaceLocalMeasurement(measurement, measurementId)
                            }
                            .onErrorCompletableAndConsumeNetworkUnavailable {
                                measurementStore.setStateForMeasurement(
                                    measurementId,
                                    LocalModelState.ERROR
                                )
                            }
                    }
            )
            .subscribeOn(schedulers.io)
    }

    override fun updateLocationForMeasurement(
        measurementId: Long,
        gpsPoint: GpsPoint?
    ): Completable {
        return measurementStore.updateGpsForMeasurement(measurementId, gpsPoint)
            .subscribeOn(schedulers.io)
    }

    private fun uploadMeasurementMedia(measurementId: Long): Completable {
        return measurementStore.getNotSyncedMedias(measurementId)
            .flatMapCompletable { medias ->
                Completable.merge(medias.map(::uploadMedia))
            }
    }

    private fun uploadMedia(localMedia: LocalMedia): Completable {
        val file = localMedia.cachedFilePath
            ?.let { mediaPath ->
                File(context.getExternalFilesDir(null), mediaPath)
            }
            ?: return Completable.complete()

        return mediaStore.setStateForMedia(
            mediaId = localMedia.localId,
            state = LocalModelState.PENDING
        )
            .andThen(mediaApi.uploadFile(file.toFormData()))
            .flatMapCompletable { remoteMedia ->
                measurementStore.updateMedia(
                    mediaLocalId = localMedia.localId,
                    remoteId = remoteMedia.id
                )
            }
            .onErrorCompletableAndConsumeNetworkUnavailable {
                mediaStore.setStateForMedia(
                    mediaId = localMedia.localId,
                    state = LocalModelState.ERROR
                )
            }
            .subscribeOn(schedulers.io)
    }

    override fun updateMorphology(measurementId: Long, morphology: Morphology): Completable {
        return morphologyStore.saveMorphologies(
            listOf(
                LocalMorphology(
                    localId = morphology.localId,
                    measurementId = measurementId,
                    groupId = morphology.groupId,
                    subgroupId = morphology.subGroupId,
                    dailyGainVolume = morphology.dailyGainVolume,
                    dailyGainWeight = morphology.dailyGainWeight,
                    draftState = morphology.draftState
                )
            )
        )
            .subscribeOn(schedulers.io)
    }

    override fun updateMedia(
        mediaLocalId: Long,
        location: GpsPoint?,
        filePath: String,
        date: Instant
    ): Completable {
        return measurementStore.updateMedia(mediaLocalId, location, filePath, date)
            .subscribeOn(schedulers.io)
    }

    override fun updateMeasurement(
        measurementId: Long,
        separateContainers: List<SeparateContainerComposite>,
        mixedContainers: List<MixedWasteContainerComposite>,
        morphologyItems: List<MorphologyItem>,
        comment: String
    ): Completable {
        return Completable.merge(
            listOf(
                saveContainerWasteTypes(separateContainers)
                    .andThen(updateSeparateContainers(separateContainers)),
                updateMixedContainers(mixedContainers),
                updateMorphologicalCategories(measurementId, morphologyItems),
                measurementStore.updateCommentForMeasurement(measurementId, comment)
            )
        )
            .subscribeOn(schedulers.io)
    }

    override fun updateSeparateContainers(
        containers: List<SeparateContainerComposite>
    ): Completable {
        return measurementStore.getSeparateContainersByIds(containers.map { it.localId })
            .flatMapCompletable { localContainers ->
                val updatedContainers = mergeSeparateContainers(
                    localContainers = localContainers,
                    containersToSave = containers
                )
                measurementStore.saveSeparateContainers(updatedContainers)
                    .ignoreElement()
            }
            .subscribeOn(schedulers.io)
    }

    private fun mergeSeparateContainers(
        localContainers: List<LocalSeparateWasteContainer>,
        containersToSave: List<SeparateContainerComposite>
    ): List<LocalSeparateWasteContainer> {
        val localIdToUpdateMap = containersToSave.associateBy { it.localId }
        return localContainers.mapNotNull { localContainer ->
            separateWasteContainerDbMerger.map(
                localIdToUpdateMap[localContainer.localId] ?: return@mapNotNull null,
                localContainer
            )
        }
    }

    private fun saveContainerWasteTypes(
        separateContainers: List<SeparateContainerComposite>
    ): Completable {
        return separateContainers.groupBy(
            keySelector = { it.localId },
            valueTransform = { it.wasteTypes }
        )
            .mapValues { it.value.flatten() }
            .let { containerIdToWasteTypes ->
                Completable.merge(
                    containerIdToWasteTypes.map { (containerId, wasteTypesToSave) ->
                        measurementStore.saveContainerWasteTypes(
                            containerWasteTypeDbMapper.map(wasteTypesToSave, containerId)
                        )
                    }
                )
            }
    }

    override fun setMeasurementImpossible(
        mnoId: String,
        impossibilityReason: String,
        gpsPoint: GpsPoint?,
        date: Instant
    ): Completable {
        return saveSkippedMeasurement(
            mnoId = mnoId,
            gpsPoint = gpsPoint,
            impossibilityReason = impossibilityReason,
            season = getSeasonString(date.month()),
            date = date
        )
            .flatMapCompletable { savedMeasurementLocalId ->
                mnoStore.getTaskIdForMno(mnoId)
                    .alsoCompletable {
                        measurementStore.setStateForMeasurement(
                            savedMeasurementLocalId,
                            LocalModelState.PENDING
                        )
                    }
                    .flatMap { taskId ->
                        sendSkippedMeasurement(
                            mnoId = mnoId,
                            gpsPoint = gpsPoint,
                            impossibilityReason = impossibilityReason,
                            date = date,
                            taskId = taskId
                        )
                    }
                    .flatMapCompletable { newMeasurement ->
                        replaceLocalMeasurement(newMeasurement, savedMeasurementLocalId)
                    }
                    .onErrorCompletableAndConsumeNetworkUnavailable {
                        measurementStore.setStateForMeasurement(
                            savedMeasurementLocalId,
                            LocalModelState.ERROR
                        )
                    }
                    .andThen(
                        measurementStore.setStateForMeasurement(
                            savedMeasurementLocalId,
                            LocalModelState.SUCCESS
                        )
                    )
            }
            .subscribeOn(schedulers.io)
    }

    override fun updateMixedContainer(container: MixedWasteContainerComposite): Completable {
        return measurementStore.getMixedWasteContainerById(containerId = container.localId)
            .map { it.container }
            .flatMapCompletable { localMixedContainer ->
                measurementStore.saveMixedContainers(
                    listOf(
                        mixedWasteContainerCompositeDbMerger.map(
                            container,
                            localMixedContainer
                        )
                    )
                )
            }
            .subscribeOn(schedulers.io)
    }

    override fun saveMixedContainer(
        measurementId: Long,
        container: MixedWasteContainerComposite
    ): Completable {
        return measurementStore.saveMixedContainers(
            listOf(
                mixedWasteContainerCompositeDbMapper.map(container, measurementId)
            )
        )
            .subscribeOn(schedulers.io)
    }

    override fun observeContainerWasteTypesByContainerId(containerLocalId: Long): Observable<List<ContainerWasteType>> {
        return measurementStore.observeContainerWasteTypesByContainerId(containerLocalId)
            .map(containerWasteTypeMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }

    override fun observeMorphologiesByMeasurementId(measurementId: Long): Observable<List<MorphologyItem>> {
        return morphologyStore.observeMorphologiesByMeasurementId(measurementId)
            .map(morphologyMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }

    override fun observeMeasurementComposite(measurementLocalId: Long): Observable<MeasurementComposite> {
        return measurementStore.observeMeasurementById(measurementLocalId)
            .map { it.filterOutDeletedEntities() }
            .map(measurementCompositeMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }

    override fun observeMeasurementList(): Observable<List<Measurement>> {
        return measurementStore.observeAllMeasurements()
            .map(measurementWithRelationsMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }

    override fun observeMeasurementsByMnoIds(mnoIds: List<String>): Observable<List<Measurement>> {
        return measurementStore.observeMeasurementsByMnoIds(mnoIds)
            .map(measurementWithRelationsMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }

    override fun observeMeasurementByLocalId(id: Long): Observable<Measurement> {
        return measurementStore.observeMeasurementById(id)
            .map { it.filterOutDeletedEntities() }
            .map(measurementWithRelationsMapper::map)
            .distinctUntilChanged()
            .subscribeOn(schedulers.io)
    }

    override fun checkMeasurementForChanges(measurementId: Long): Single<Boolean> {
        return measurementStore.observeMeasurementById(measurementId)
            .firstOrError()
            .map { it.hasChanges() }
            .subscribeOn(schedulers.io)
    }

    override fun deleteMeasurementById(measurementId: Long): Completable {
        return measurementStore.deleteMeasurementWithRelationsBy(measurementId)
            .subscribeOn(schedulers.io)
    }

    override fun setMixedContainerDeleted(containerId: Long): Completable {
        return setEntityDeleted(
            fetchAction = { measurementStore.getMixedWasteContainerById(containerId) },
            isDeletable = { it.container.draftState != DraftState.IDLE },
            delete = { measurementStore.deleteMixedContainer(containerId) },
            setDeleted = { measurementDraftStore.setMixedContainerDeleted(containerId) }
        )
            .subscribeOn(schedulers.io)
    }

    override fun setSeparateContainerDeleted(containerId: Long): Completable {
        return setEntityDeleted(
            fetchAction = { measurementStore.getSeparateWasteContainerById(containerId) },
            isDeletable = { it.container.draftState != DraftState.IDLE },
            delete = { measurementStore.deleteSeparateContainerWithRelations(containerId) },
            setDeleted = { measurementDraftStore.setSeparateContainerDeleted(containerId) }
        )
            .subscribeOn(schedulers.io)
    }

    override fun setMediaDeleted(mediaId: Long): Completable {
        return setEntityDeleted(
            fetchAction = { measurementStore.getMediaById(mediaId) },
            isDeletable = { it.draftState != DraftState.IDLE },
            delete = { measurementStore.deleteMediaWithRelations(mediaId) },
            setDeleted = { measurementDraftStore.setMediaDeleted(mediaId) }
        )
            .subscribeOn(schedulers.io)
    }

    override fun setWasteTypeDeleted(wasteTypeId: String): Completable {
        return setEntityDeleted(
            fetchAction = { measurementStore.getContainerWasteTypeById(wasteTypeId) },
            isDeletable = { it.wasteType.draftState != DraftState.IDLE },
            delete = { measurementStore.deleteContainerWasteType(wasteTypeId) },
            setDeleted = { measurementDraftStore.setContainerWasteTypeDeleted(wasteTypeId) }
        )
    }

    override fun setMorphologyItemDeleted(morphologyItemId: Long): Completable {
        return setEntityDeleted(
            fetchAction = { morphologyStore.getMorphologyById(morphologyItemId) },
            isDeletable = { it.morphology.draftState != DraftState.IDLE },
            delete = { morphologyStore.deleteMorphology(morphologyItemId) },
            setDeleted = { measurementDraftStore.setMorphologyItemDeleted(morphologyItemId) }
        )
    }

    override fun rollbackChangedMeasurementEntities(measurementId: Long): Completable {
        return measurementDraftStore.rollbackChangedMeasurementEntities(measurementId)
            .subscribeOn(schedulers.io)
    }

    override fun rollbackChangedSeparateContainer(containerId: Long): Completable {
        return measurementDraftStore.rollbackChangedWasteTypesForContainer(containerId)
            .subscribeOn(schedulers.io)
    }

    override fun approveMeasurementChanges(measurementId: Long): Completable {
        return measurementDraftStore.approveMeasurementChanges(measurementId)
            .subscribeOn(schedulers.io)
    }

    override fun deleteMediaWithRelations(mediaLocalId: Long): Completable {
        val deleteCachedMediaCompletable = measurementStore.getMediaById(mediaLocalId)
            .toSingle()
            .doOnSuccess { localMedia ->
                localMedia.cachedFilePath
                    ?.let { mediaFilePath ->
                        File(context.getExternalFilesDir(null), mediaFilePath)
                    }
                    ?.delete()
            }
            .ignoreElement()

        return deleteCachedMediaCompletable.andThen(
            measurementStore.deleteMediaWithRelations(mediaLocalId)
        )
            .subscribeOn(schedulers.io)
    }

    override fun deleteSeparateWasteContainer(containerLocalId: Long): Completable {
        return measurementStore.deleteSeparateContainerWithRelations(containerLocalId)
            .subscribeOn(schedulers.io)
    }

    override fun deleteMorphologyById(categoryId: Long): Completable {
        return morphologyStore.deleteMorphology(categoryId)
            .subscribeOn(schedulers.io)
    }

    private fun saveSkippedMeasurement(
        mnoId: String,
        gpsPoint: GpsPoint?,
        impossibilityReason: String,
        season: String,
        date: Instant
    ): Single<Long> {
        val skippedMeasurement = LocalMeasurement(
            mnoId = mnoId,
            remoteId = null,
            comment = null,
            gpsPoint = gpsPoint,
            measurementStatusId = MODERATION_STATUS_ID,
            date = date,
            isPossible = false,
            season = season,
            impossibilityReason = impossibilityReason,
            revisionComment = null,
            state = LocalModelState.SUCCESS
        )
        return measurementStore.saveMeasurements(listOf(skippedMeasurement))
            .map { it.first() }
    }

    private fun sendSkippedMeasurement(
        mnoId: String,
        taskId: String,
        gpsPoint: GpsPoint?,
        impossibilityReason: String,
        date: Instant
    ): Maybe<RemoteMeasurement> {
        val measurementToSend = MeasurementToSend(
            measurementId = null,
            mnoId = mnoId,
            taskId = taskId,
            latitude = gpsPoint?.lat,
            longitude = gpsPoint?.lng,
            date = date,
            isPossible = false,
            impossibilityReason = impossibilityReason,
            containers = emptyList(),
            morphology = emptyList(),
            photos = null,
            videos = null,
            measurementActPhotos = null,
            comment = null
        )
        return measurementNetworkApi.sendMeasurement(measurementToSend)
    }

    private fun replaceLocalMeasurement(
        remoteMeasurement: RemoteMeasurement,
        measurementLocalId: Long
    ): Completable {
        val measurementToSave = measurementNetworkMapper.map(remoteMeasurement)
        return Completable.fromCallable {
            dataBase.runInTransaction {
                measurementStore.deleteMeasurementWithRelationsBy(measurementLocalId)
                    .andThen(measurementSaveDelegate.saveMeasurements(listOf(measurementToSave)))
                    .blockingAwait()
            }
        }
    }

    private fun updateMixedContainers(containers: List<MixedWasteContainerComposite>): Completable {
        return measurementStore.getMixedContainersByIds(containers.map { it.localId })
            .flatMapCompletable { localContainers ->
                val localIdToContainerMap = containers.associateBy { it.localId }
                localContainers.associateBy { it.localId }
                    .mapNotNull { (localId, localContainer) ->
                        mixedWasteContainerCompositeDbMerger.map(
                            localIdToContainerMap[localId] ?: return@mapNotNull null,
                            localContainer
                        )
                    }
                    .let(measurementStore::saveMixedContainers)
            }
    }

    private fun updateMorphologicalCategories(
        measurementId: Long,
        morphologies: List<MorphologyItem>
    ): Completable {
        return morphologyStore.saveMorphologies(
            morphologyDbMapper.map(morphologies, measurementId)
        )
            .subscribeOn(schedulers.io)
    }

    private fun getSeasonString(month: Month): String {
        return Season.getSeason(month).getValue(resources)
    }

    private fun Instant.month(): Month {
        return atZone(ZoneId.systemDefault()).month
    }

    private fun LocalMeasurementWithRelations.filterOutDeletedEntities(): LocalMeasurementWithRelations {
        return this.copy(
            mixedWasteContainers = mixedWasteContainers.filter {
                it.container.draftState != DraftState.DELETED
            },
            separateWasteContainers = separateWasteContainers.filter {
                it.container.draftState != DraftState.DELETED
            }.map { separateContainer ->
                val filteredWasteTypes = separateContainer.wasteTypes
                    .filter { it.wasteType.draftState != DraftState.DELETED }

                separateContainer.copy(
                    wasteTypes = filteredWasteTypes
                )
            },
            morphologyList = morphologyList.filter {
                it.morphology.draftState != DraftState.DELETED
            },
            medias = medias.filter {
                it.media.draftState != DraftState.DELETED
            }
        )
    }

    private fun LocalMeasurementWithRelations.hasChanges(): Boolean {
        return mixedWasteContainers.any { it.container.draftState != DraftState.IDLE } ||
            separateWasteContainers.any { it.container.draftState != DraftState.IDLE } ||
            separateWasteContainers.flatMap { it.wasteTypes }
                .any { it.wasteType.draftState != DraftState.IDLE } ||
            morphologyList.any { it.morphology.draftState != DraftState.IDLE } ||
            medias.any { it.media.draftState != DraftState.IDLE }
    }

    private fun <T> setEntityDeleted(
        fetchAction: () -> Maybe<T>,
        isDeletable: (T) -> Boolean,
        delete: () -> Completable,
        setDeleted: () -> Completable
    ): Completable {
        return fetchAction()
            .flatMapCompletable { entity ->
                if (isDeletable(entity)) delete() else setDeleted()
            }
            .subscribeOn(schedulers.io)
    }
}