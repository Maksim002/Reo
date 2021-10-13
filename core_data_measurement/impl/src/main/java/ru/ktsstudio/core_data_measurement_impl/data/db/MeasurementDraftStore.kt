package ru.ktsstudio.core_data_measurement_impl.data.db

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.utils.db.batchedCompletable
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementDraftDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MediaDao
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMedia
import javax.inject.Inject

@OptIn(ExperimentalStdlibApi::class)
class MeasurementDraftStore @Inject constructor(
    private val db: MeasurementDb,
    private val measurementDao: MeasurementDao,
    private val measurementContainerDao: MeasurementContainerDao,
    private val measurementDraftDao: MeasurementDraftDao,
    private val mediaDao: MediaDao
) {

    fun approveMeasurementChanges(measurementId: Long): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                approveModifiedWasteTypesFor(measurementId)
                    .andThen(approveModifiedSeparateContainersFor(measurementId))
                    .andThen(approveModifiedMixedContainersFor(measurementId))
                    .andThen(approveModifiedMorphologyFor(measurementId))
                    .andThen(approveModifiedMediasFor(measurementId))
                    .blockingAwait()
            }
        }
    }

    fun rollbackChangedMeasurementEntities(measurementId: Long): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                rollbackModifiedMixedContainersFor(measurementId).blockingAwait()
                rollbackModifiedSeparateContainersFor(measurementId)
                    .andThen(rollbackChangedWasteTypesForMeasurement(measurementId))
                    .blockingAwait()
                rollbackModifiedMorphologyFor(measurementId).blockingAwait()
                rollBackModifiedMediasFor(measurementId).blockingAwait()
            }
        }
    }

    private fun rollbackChangedWasteTypesForMeasurement(measurementId: Long): Completable {
        return measurementContainerDao.getSeparateWasteContainersBy(measurementId)
            .map { it.map { it.localId } }
            .flatMapCompletable { containerIds ->
                Completable.merge(
                    listOf(
                        batchedCompletable(
                            list = containerIds,
                            query = measurementDraftDao::rollbackDeletedWasteTypesFor
                        ),
                        batchedCompletable(
                            list = containerIds,
                            query = measurementDraftDao::removeAddedWasteTypesFor
                        )
                    )
                )
            }
    }

    fun rollbackChangedWasteTypesForContainer(containerId: Long): Completable {
        return Completable.merge(
            listOf(
                measurementDraftDao.rollbackDeletedWasteTypesFor(listOf(containerId)),
                measurementDraftDao.removeAddedWasteTypesFor(listOf(containerId))
            )
        )
    }

    fun setMixedContainerDeleted(containerId: Long): Completable {
        return measurementDraftDao.setMixedContainerDraftState(containerId, DraftState.DELETED)
    }

    fun setSeparateContainerDeleted(containerId: Long): Completable {
        return measurementDraftDao.setSeparateContainerDraftState(containerId, DraftState.DELETED)
    }

    fun setContainerWasteTypeDeleted(wasteTypeId: String): Completable {
        return measurementDraftDao.setContainerWasteTypeDraftState(wasteTypeId, DraftState.DELETED)
    }

    fun setMorphologyItemDeleted(morphologyItemId: Long): Completable {
        return measurementDraftDao.setMorphologyItemDraftState(morphologyItemId, DraftState.DELETED)
    }

    fun setMediaDeleted(mediaId: Long): Completable {
        return measurementDraftDao.setMediaDraftState(mediaId, DraftState.DELETED)
    }

    private fun deleteAddedSeparateContainers(measurementId: Long): Completable {
        return deleteSeparateContainers {
            measurementDraftDao.getAddedSeparateWasteContainersBy(measurementId)
                .map { it.map { it.localId } }
        }
    }

    private fun removeDeletedSeparateContainers(measurementId: Long): Completable {
        return deleteSeparateContainers {
            measurementDraftDao.getDeletedSeparateWasteContainersBy(measurementId)
                .map { it.map { it.localId } }
        }
    }

    private fun deleteAddedMedias(measurementId: Long): Completable {
        return updateMeasurementMedias(
            measurementId = measurementId,
            requiredMediaPredicate = { it.draftState == DraftState.ADDED },
            updateAction = { mediaId ->
                Completable.fromCallable {
                    db.runInTransaction {
                        measurementDao.deleteMeasurementMediaByMediaId(mediaId).blockingAwait()
                        mediaDao.deleteMediaById(mediaId).blockingAwait()
                    }
                }
            }
        )
    }

    private fun rollBackDeletedMedias(measurementId: Long): Completable {
        return updateMeasurementMedias(
            measurementId = measurementId,
            requiredMediaPredicate = { it.draftState == DraftState.DELETED },
            updateAction = { mediaId ->
                measurementDraftDao.setMediaDraftState(mediaId, DraftState.IDLE)
            }
        )
    }

    private fun removeDeletedMedias(measurementId: Long): Completable {
        return updateMeasurementMedias(
            measurementId = measurementId,
            requiredMediaPredicate = { it.draftState == DraftState.DELETED },
            updateAction = { mediaId ->
                Completable.fromCallable {
                    db.runInTransaction {
                        measurementDao.deleteMeasurementMediaByMediaId(mediaId).blockingAwait()
                        mediaDao.deleteMediaById(mediaId).blockingAwait()
                    }
                }
            }
        )
    }

    private fun approveAddedMedias(measurementId: Long): Completable {
        return updateMeasurementMedias(
            measurementId = measurementId,
            requiredMediaPredicate = { it.draftState == DraftState.ADDED },
            updateAction = { mediaId ->
                measurementDraftDao.setMediaDraftState(mediaId, DraftState.IDLE)
            }
        )
    }

    private fun updateMeasurementMedias(
        measurementId: Long,
        requiredMediaPredicate: (LocalMedia) -> Boolean,
        updateAction: (Long) -> Completable
    ): Completable {
        return measurementDao.getMeasurementMedias(measurementId)
            .flatMapCompletable { measurementMedias ->
                Completable.merge(
                    measurementMedias.filter {
                        requiredMediaPredicate(it)
                    }
                        .map { it.localId }
                        .map { localId ->
                            updateAction(localId)
                        }
                )
            }
    }

    private fun deleteSeparateContainers(
        getSeparateContainerIdsToDelete: () -> Single<List<Long>>
    ): Completable {
        return getSeparateContainerIdsToDelete().flatMapCompletable { containerIdsToDelete ->
            Completable.fromCallable {
                db.runInTransaction {
                    batchedCompletable(
                        list = containerIdsToDelete,
                        query = measurementContainerDao::deleteContainerWasteTypesByContainerIds
                    ).blockingAwait()
                    measurementContainerDao.deleteSeparateContainersByIds(
                        containerIdsToDelete
                    ).blockingAwait()
                }
            }
        }
    }

    private fun rollbackModifiedMixedContainersFor(measurementId: Long): Completable {
        return Completable.merge(
            listOf(
                measurementDraftDao.rollbackDeletedMixedContainersFor(measurementId),
                measurementDraftDao.removeAddedMixedContainersFor(measurementId)
            )
        )
    }

    private fun rollbackModifiedSeparateContainersFor(measurementId: Long): Completable {
        return Completable.merge(
            listOf(
                measurementDraftDao.rollbackDeletedSeparateContainersFor(measurementId),
                deleteAddedSeparateContainers(measurementId)
            )
        )
    }

    private fun rollbackModifiedMorphologyFor(measurementId: Long): Completable {
        return Completable.merge(
            listOf(
                measurementDraftDao.rollbackDeletedMorphologyItemsFor(measurementId),
                measurementDraftDao.removeAddedMorphologyItemsFor(measurementId)
            )
        )
    }

    private fun rollBackModifiedMediasFor(measurementId: Long): Completable {
        return Completable.merge(
            listOf(
                rollBackDeletedMedias(measurementId),
                deleteAddedMedias(measurementId)
            )
        )
    }

    private fun approveModifiedMixedContainersFor(measurementId: Long): Completable {
        return Completable.merge(
            listOf(
                measurementDraftDao.removeDeletedMixedContainersFor(measurementId),
                measurementDraftDao.approveAddedMixedContainersFor(measurementId)
            )
        )
    }

    private fun approveModifiedSeparateContainersFor(measurementId: Long): Completable {
        return Completable.merge(
            listOf(
                measurementDraftDao.approveAddedSeparateContainersFor(measurementId),
                removeDeletedSeparateContainers(measurementId)
            )
        )
    }

    private fun approveModifiedWasteTypesFor(measurementId: Long): Completable {
        return measurementContainerDao.getSeparateWasteContainersBy(measurementId)
            .map { it.map { it.localId } }
            .flatMapCompletable { containerIds ->
                Completable.merge(
                    listOf(
                        batchedCompletable(
                            list = containerIds,
                            query = measurementDraftDao::removeDeletedWasteTypesFor
                        ),
                        batchedCompletable(
                            list = containerIds,
                            query = measurementDraftDao::approveAddedWasteTypesFor
                        )
                    )
                )
            }
    }

    private fun approveModifiedMorphologyFor(measurementId: Long): Completable {
        return Completable.merge(
            listOf(
                measurementDraftDao.removeDeletedMorphologyItemsFor(measurementId),
                measurementDraftDao.approveAddedMorphologyItemsFor(measurementId)
            )
        )
    }

    private fun approveModifiedMediasFor(measurementId: Long): Completable {
        return Completable.merge(
            listOf(
                removeDeletedMedias(measurementId),
                approveAddedMedias(measurementId)
            )
        )
    }
}

