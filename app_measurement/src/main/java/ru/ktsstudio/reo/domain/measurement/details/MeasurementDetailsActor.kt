package ru.ktsstudio.reo.domain.measurement.details

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.subjects.PublishSubject
import ru.ktsstudio.common.utils.mergeItems
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.delayFirst
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.reo.domain.measurement.common.MeasurementDraftHolder
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.Effect
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.State
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.Wish

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
internal class MeasurementDetailsActor(
    private val repository: MeasurementRepository,
    private val schedulerProvider: SchedulerProvider,
    private val draftHolder: MeasurementDraftHolder?
) : Actor<State, Wish, Effect> {
    private val interruptSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        return when (action) {
            is Wish.Load -> observeDetails(action.measurementId, action.isPreviewMode)
            is Wish.CreateMeasurement -> {
                val measurementId = state.data
                    ?.measurementLocalId
                    ?: error("Measurement ID is null")

                createMeasurement(measurementId)
            }
            is Wish.EditMeasurement -> {
                val measurement = state.data
                    ?: error("Measurement is null")

                Observable.just(
                    Effect.OpenEditMeasurement(
                        measurement.mnoId,
                        measurement.measurementLocalId
                    )
                )
            }
        }
    }

    private fun observeDetails(measurementId: Long, isPreviewMode: Boolean): Observable<Effect> {
        stopObserving()
        return repository.observeMeasurementByLocalId(measurementId)
            .map { measurement ->
                measurement.mergeWithSessionDraft()
            }
            .delayFirst(DELAY)
            .map<Effect> { measurement ->
                val deadLine = measurement.date + Duration.ofHours(12)
                val isEditable = measurement.status.id == MeasurementRepository.REVISION_STATUS_ID &&
                        Instant.now() < deadLine
                Effect.Success(
                    measurement,
                    isEditable = isEditable
                )
            }
            .startWithIterable(
                listOf(
                    Effect.Loading,
                    Effect.PreviewModeInfo(isPreviewMode)
                )
            )
            .onErrorReturn(Effect::Error)
            .observeOn(schedulerProvider.ui)
            .toRx2Observable()
            .takeUntil(interruptSignal)
    }

    private fun createMeasurement(measurementId: Long): Observable<Effect> {
        return repository.approveMeasurementChanges(measurementId)
            .andThen(rewriteMeasurementDraft(measurementId))
            .andThen(repository.uploadMeasurement(measurementId))
            .andThen(Rx3Observable.just<Effect>(Effect.MeasurementCreated))
            .startWithItem(Effect.MeasurementCreateInProcess)
            .onErrorReturn(Effect::MeasurementCreateFailed)
            .observeOn(schedulerProvider.ui)
            .toRx2Observable()
    }

    private fun stopObserving() = interruptSignal.onNext(Unit)

    private fun rewriteMeasurementDraft(measurementId: Long): Completable {
        val draft = draftHolder?.getSessionMeasurementDraft()
        return if (draft != null) {
            repository.updateMeasurement(
                measurementId = measurementId,
                separateContainers = draft.separateContainers.toList(),
                mixedContainers = draft.mixedContainers.toList(),
                morphologyItems = draft.morphologyItems.toList(),
                comment = draft.comment
            )
        } else {
            Completable.complete()
        }
    }

    private fun Measurement.mergeWithSessionDraft(): Measurement {
        val sessionDraft = draftHolder?.getSessionMeasurementDraft()
            ?.takeIf { draftHolder.isEmpty().not() }
            ?: return this

        val separateContainerIdToDraftMap = sessionDraft.separateContainers
            .associateBy { it.localId }
        val mixedContainerIdToDraftMap = sessionDraft.mixedContainers
            .associateBy { it.localId }

        return this.copy(
            morphologyList = mergeItems(
                higherPriorityList = sessionDraft.morphologyItems.toList(),
                lowerPriorityList = morphologyList,
                keySelector = { it.localId }
            ),
            separateWasteContainers = separateWasteContainers.map { container ->
                val draft = separateContainerIdToDraftMap[container.localId]
                    ?: return@map container
                container.copy(
                    isUnique = draft.isUnique,
                    containerName = draft.getName(),
                    containerVolume = draft.getVolume(),
                    wasteTypes = mergeItems(
                        higherPriorityList = draft.wasteTypes,
                        lowerPriorityList = container.wasteTypes.orEmpty(),
                        keySelector = { it.localId }
                    )
                )
            },
            mixedWasteContainers = mixedWasteContainers.map { container ->
                val draft = mixedContainerIdToDraftMap[container.localId]
                    ?: return@map container

                container.copy(
                    isUnique = draft.isUnique,
                    containerName = draft.getName(),
                    containerVolume = draft.getVolume(),
                    containerFullness = draft.containerFullness,
                    wasteVolume = draft.wasteVolume,
                    dailyGainVolume = draft.dailyGainVolume,
                    netWeight = draft.netWeight,
                    dailyGainNetWeight = draft.dailyGainNetWeight,
                    emptyContainerWeight = draft.emptyContainerWeight,
                    filledContainerWeight = draft.filledContainerWeight
                )
            },
            comment = sessionDraft.comment
        )
    }

    companion object {
        private const val DELAY = 500L
    }
}
