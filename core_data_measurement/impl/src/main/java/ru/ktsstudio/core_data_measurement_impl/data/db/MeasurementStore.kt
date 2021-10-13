package ru.ktsstudio.core_data_measurement_impl.data.db

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.common.utils.db.batchedCompletable
import ru.ktsstudio.common.utils.db.batchedQuerySingle
import ru.ktsstudio.common.utils.rx.alsoCompletable
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MediaDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MorphologyDao
import ru.ktsstudio.core_data_measurement_impl.data.db.model.CompositeMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteTypeWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurement
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMediaPayload
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.MediaType
import javax.inject.Inject

@OptIn(ExperimentalStdlibApi::class)
class MeasurementStore @Inject constructor(
    private val db: MeasurementDb,
    private val measurementDao: MeasurementDao,
    private val measurementContainerDao: MeasurementContainerDao,
    private val morphologyDao: MorphologyDao,
    private val mediaDao: MediaDao
) {

    fun observeMeasurementById(measurementLocalId: Long): Observable<LocalMeasurementWithRelations> {
        return measurementDao.observeMeasurementWithRelationsById(measurementLocalId)
    }

    fun observeContainerWasteTypesByContainerId(containerId: Long): Observable<List<LocalContainerWasteTypeWithRelation>> {
        return measurementContainerDao.observeContainerWasteTypesBy(containerId)
    }

    fun observeAllMeasurements(): Observable<List<LocalMeasurementWithRelations>> {
        return measurementDao.observeAll()
    }

    fun observeMeasurementsByMnoIds(mnoIds: List<String>): Observable<List<LocalMeasurementWithRelations>> {
        return measurementDao.observeByMnoIds(mnoIds)
    }

    fun createMediaForMeasurement(
        measurementId: Long,
        category: MeasurementMediaCategory
    ): Single<Long> {
        return mediaDao.insertLocalMedias(listOf(createEmptyMedia(category)))
            .map { it.first() }
            .alsoCompletable { mediaLocalId ->
                val measurementMedia = LocalMeasurementMedia(
                    measurementId = measurementId,
                    mediaLocalId = mediaLocalId,
                    mediaCategory = category
                )
                measurementDao.insertMeasurementsMedia(listOf(measurementMedia))
            }
    }

    fun deleteMeasurementWithRelationsBy(measurementLocalId: Long): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                morphologyDao.deleteCategoriesByMeasurementId(measurementLocalId)
                    .blockingAwait()
                measurementDao.deleteMeasurementById(measurementLocalId).blockingAwait()
                measurementContainerDao.deleteMixedWasteContainersByMeasurementId(measurementLocalId)
                    .blockingAwait()
                deleteSeparateWasteContainersBy(measurementLocalId).blockingAwait()
                measurementDao.deleteMediaByMeasurementId(measurementLocalId).blockingAwait()
            }
        }
    }

    fun deleteSeparateContainerWithRelations(containerId: Long): Completable {
        return measurementContainerDao.deleteContainerWasteTypesByContainerIds(listOf(containerId))
            .andThen(measurementContainerDao.deleteSeparateContainerById(containerId))
    }

    fun deleteContainerWasteType(wasteTypeId: String): Completable {
        return measurementContainerDao.deleteContainerWasteTypeById(wasteTypeId)
    }

    fun deleteMixedContainer(containerId: Long): Completable {
        return measurementContainerDao.deleteMixedWasteContainerById(containerId)
    }

    fun deleteMediaWithRelations(mediaLocalId: Long): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                measurementDao.deleteMeasurementMediaByMediaId(mediaLocalId).blockingAwait()
                mediaDao.deleteMediaById(mediaLocalId).blockingAwait()
            }
        }
    }

    fun clear(): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                morphologyDao.clear().blockingAwait()
                measurementDao.clearMeasurements().blockingAwait()
                measurementDao.clearMeasurementMedia().blockingAwait()
                measurementDao.clearMeasurementStatuses().blockingAwait()
                measurementContainerDao.clearContainerWasteTypes().blockingAwait()
                measurementContainerDao.clearMixedContainers().blockingAwait()
                measurementContainerDao.clearSeparateWasteContainers().blockingAwait()
                mediaDao.clearMedia().blockingAwait()
            }
        }
    }

    fun getNotSyncedMeasurementIds(): Single<List<Long>> {
        return measurementDao.getNotSyncedMeasurements()
            .map { it.map { it.localId } }
    }

    fun getContainerTypes(): Single<List<LocalContainerType>> {
        return measurementContainerDao.getContainerTypes()
    }

    fun getWasteCategories(): Single<List<LocalWasteCategory>> {
        return measurementContainerDao.getWasteCategories()
    }

    fun getContainerTypeById(typeId: String): Maybe<LocalContainerType> {
        return measurementContainerDao.getContainerTypeById(typeId)
    }

    fun getNotSyncedMedias(measurementId: Long): Single<List<LocalMedia>> {
        return measurementDao.getNotSyncedMedias(measurementId)
    }

    fun getContainerWasteTypeById(wasteTypeId: String): Maybe<LocalContainerWasteTypeWithRelation> {
        return measurementContainerDao.getContainerWasteTypeBy(wasteTypeId)
    }

    fun getSeparateWasteContainerById(containerId: Long): Maybe<LocalSeparateWasteContainerWithRelations> {
        return measurementContainerDao.getSeparateWasteContainerById(containerId)
    }

    fun getMixedWasteContainerById(containerId: Long): Maybe<LocalMixedWasteContainerWithRelations> {
        return measurementContainerDao.getMixedWasteContainerById(containerId)
    }

    fun getMixedContainersByIds(containerIds: List<Long>): Single<List<LocalMixedWasteContainer>> {
        return containerIds.batchedQuerySingle(measurementContainerDao::getMixedWasteContainersByIds)
    }

    fun getSeparateContainersByIds(containerIds: List<Long>): Single<List<LocalSeparateWasteContainer>> {
        return containerIds.batchedQuerySingle(measurementContainerDao::getSeparateContainersByIds)
    }

    fun getMediaById(mediaId: Long): Maybe<LocalMedia> {
        return mediaDao.getByLocalId(mediaId)
    }

    fun saveContainerWasteTypes(wasteTypes: List<LocalContainerWasteType>): Completable {
        return measurementContainerDao.insertLocalContainerWasteTypes(wasteTypes)
    }

    fun saveMeasurementStatuses(statuses: List<LocalMeasurementStatus>): Completable {
        return measurementDao.insertMeasurementStatuses(statuses)
    }

    fun saveMeasurements(measurements: List<LocalMeasurement>): Single<List<Long>> {
        return measurementDao.insertMeasurements(measurements)
    }

    fun saveMixedContainers(containers: List<LocalMixedWasteContainer>): Completable {
        return measurementContainerDao.insertLocalMixedContainers(containers)
    }

    fun saveMedia(compositeMedia: List<CompositeMedia>): Completable {
        return Observable.fromIterable(compositeMedia)
            .flatMapCompletable(::saveMedia)
    }

    fun saveSeparateContainers(containers: List<LocalSeparateWasteContainer>): Single<List<Long>> {
        return measurementContainerDao.insertLocalSeparateWasteContainers(containers)
    }

    private fun saveMedia(compositeMedia: CompositeMedia): Completable {
        fun createMeasurementMedia(mediaIds: List<Long>, mediaCategory: MeasurementMediaCategory) =
            mediaIds.map { mediaId ->
                LocalMeasurementMedia(
                    measurementId = compositeMedia.measurementId,
                    mediaCategory = mediaCategory,
                    mediaLocalId = mediaId
                )
            }

        return Single.zip(
            updateMedias(compositeMedia.photos),
            updateMedias(compositeMedia.videos),
            updateMedias(compositeMedia.actPhotos),
            { photoIds, videoIds, actPhotoIds ->
                buildList {
                    addAll(createMeasurementMedia(photoIds, MeasurementMediaCategory.PHOTO))
                    addAll(createMeasurementMedia(videoIds, MeasurementMediaCategory.VIDEO))
                    addAll(createMeasurementMedia(actPhotoIds, MeasurementMediaCategory.ACT_PHOTO))
                }
            }
        )
            .flatMapCompletable(measurementDao::insertMeasurementsMedia)
    }

    fun setStateForMeasurement(
        measurementId: Long,
        state: LocalModelState
    ): Completable = measurementDao.updateStateForMeasurement(measurementId, state)

    fun updateCommentForMeasurement(measurementId: Long, comment: String): Completable {
        return measurementDao.updateCommentForMeasurement(measurementId, comment)
    }

    fun updateGpsForMeasurement(measurementId: Long, gpsPoint: GpsPoint?): Completable {
        return measurementDao.updateGpsForMeasurement(measurementId, gpsPoint)
    }

    fun updateMedia(
        mediaLocalId: Long,
        location: GpsPoint?,
        filePath: String,
        date: Instant
    ): Completable {
        val payload = LocalMediaPayload(
            localId = mediaLocalId,
            gpsPoint = location,
            cachedFilePath = filePath,
            date = date
        )
        return mediaDao.updateMedia(payload)
    }

    fun updateMedia(
        mediaLocalId: Long,
        remoteId: String
    ): Completable {
        return mediaDao.updateMediaRemoteId(mediaLocalId, remoteId)
    }

    private fun updateMedias(medias: List<LocalMedia>): Single<List<Long>> {
        return Single.fromCallable {
            medias.partition { it.remoteId.isBlank() }
        }
            .flatMap { (localMedias, remoteMedias) ->
                Single.zip(
                    mediaDao.insertLocalMedias(localMedias),
                    updateMediasByRemoteIds(remoteMedias),
                    BiFunction { newMediaIds: List<Long>, updatedMediaIds: List<Long> ->
                        newMediaIds + updatedMediaIds
                    }
                )
            }
    }

    private fun updateMediasByRemoteIds(remoteMedias: List<LocalMedia>): Single<List<Long>> {
        return mediaDao.getByRemoteIds(remoteMedias.map { it.remoteId })
            .map { existingMedias ->
                existingMedias.associateBy { it.remoteId }
            }
            .map { remoteToLocalIdMap ->
                remoteMedias.map { mediaToSave ->
                    mediaToSave.copy(
                        localId = remoteToLocalIdMap[mediaToSave.remoteId]?.localId ?: 0,
                        cachedFilePath = remoteToLocalIdMap[mediaToSave.remoteId]?.cachedFilePath
                    )
                }
            }
            .flatMap { localMedias ->
                localMedias.batchedQuerySingle(mediaDao::insertLocalMedias)
            }
    }

    private fun deleteSeparateWasteContainersBy(measurementLocalId: Long): Completable {
        return measurementContainerDao.getSeparateWasteContainersBy(measurementLocalId)
            .map { it.map { it.localId } }
            .flatMapCompletable { containerIdsToDelete ->
                Completable.fromCallable {
                    db.runInTransaction {
                        batchedCompletable(
                            list = containerIdsToDelete,
                            query = measurementContainerDao::deleteContainerWasteTypesByContainerIds
                        ).blockingAwait()
                        batchedCompletable(
                            list = containerIdsToDelete,
                            query = measurementContainerDao::deleteSeparateContainersByIds
                        ).blockingAwait()
                    }
                }
            }
    }

    private fun createEmptyMedia(category: MeasurementMediaCategory): LocalMedia {
        return LocalMedia(
            cachedFilePath = null,
            mediaType = category.getMediaType(),
            remoteId = "",
            url = null,
            gpsPoint = null,
            date = null,
            draftState = DraftState.ADDED,
            localState = LocalModelState.SUCCESS
        )
    }

    private fun MeasurementMediaCategory.getMediaType(): MediaType {
        return when (this) {
            MeasurementMediaCategory.PHOTO,
            MeasurementMediaCategory.ACT_PHOTO -> MediaType.PHOTO
            MeasurementMediaCategory.VIDEO -> MediaType.VIDEO
        }
    }
}

