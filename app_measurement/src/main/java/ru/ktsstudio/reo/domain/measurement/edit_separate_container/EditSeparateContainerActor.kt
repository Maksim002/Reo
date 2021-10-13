package ru.ktsstudio.reo.domain.measurement.edit_separate_container

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import ru.ktsstudio.reo.domain.measurement.common.SeparateContainerDraftHolder
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.ContainerName
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.ContainerVolume
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature.Effect
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature.State
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature.Wish

/**
 * Created by Igor Park on 19/10/2020.
 */
class EditSeparateContainerActor(
    private val measurementRepository: MeasurementRepository,
    private val mnoRepository: MnoRepository,
    private val schedulers: SchedulerProvider,
    private val draftHolder: SeparateContainerDraftHolder
) : Actor<State, Wish, Effect> {

    private val interruptSignal = Rx2PublishSubject.create<Unit>()

    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        return when (action) {
            is Wish.InitData -> initAndObserveSeparateWasteContainer(action)
            is Wish.UpdateField -> updateField(
                currentState = state.separateContainer,
                dataType = action.dataType,
                value = action.value
            )
            is Wish.SaveData -> saveData(state.separateContainer)
            is Wish.DeleteData -> deleteData(state.separateContainer.localId)
            is Wish.ClearUnfinishedData -> clearUnfinishedData(state)
        }
    }

    private fun initAndObserveSeparateWasteContainer(initAction: Wish.InitData): Observable<Effect> {
        stopObservingContainer()

        return prepareSeparateContainer(
            measurementId = initAction.measurementId,
            containerId = initAction.containerId,
            mnoContainerId = initAction.mnoContainerId,
            containerTypeId = initAction.containerTypeId
        )
            .toSingle()
            .flatMapObservable<Effect>(::observeContainerWithWasteTypes)
            .startWithItem(Effect.DataLoading)
            .onErrorReturn(Effect::DataLoadError)
            .observeOn(schedulers.ui)
            .toRx2Observable()
            .takeUntil(interruptSignal)
    }

    private fun observeContainerWithWasteTypes(
        container: SeparateContainerComposite
    ): Rx3Observable<Effect> {
        return Rx3Observable.merge(
            Rx3Observable.just<Effect>(Effect.DataInitialized(container)),
            observeContainerWasteTypes(container).skip(1)
        )
    }

    private fun prepareSeparateContainer(
        measurementId: Long,
        containerId: Long?,
        mnoContainerId: String?,
        containerTypeId: String?
    ): Maybe<SeparateContainerComposite> {
        val containerExist = containerId != null
        val mnoContainerSelected = mnoContainerId != null
        val typeSelected = containerTypeId != null
        return when {
            containerExist -> {
                draftHolder.getSeparateContainerDraft(containerId!!)
                    ?.let { Maybe.just(it) }
                    ?: measurementRepository.getSeparateWasteContainerById(containerId)
            }
            mnoContainerSelected -> createContainerWithMnoContainer(measurementId, mnoContainerId!!)
            typeSelected -> createContainerWithType(measurementId, containerTypeId!!)
            else -> error("Unreachable condition")
        }
    }

    private fun createContainerWithType(
        measurementId: Long,
        containerTypeId: String
    ): Maybe<SeparateContainerComposite> {
        return measurementRepository.createSeparateWasteContainer(
            measurementId = measurementId,
            containerTypeId = containerTypeId,
            isUnique = true,
            mnoContainerId = null,
            draftState = DraftState.ADDED
        )
    }

    private fun createContainerWithMnoContainer(
        measurementId: Long,
        mnoContainerId: String
    ): Maybe<SeparateContainerComposite> {
        return mnoRepository.getMnoContainerById(mnoContainerId)
            .flatMap { mnoContainer ->
                measurementRepository.createSeparateWasteContainer(
                    measurementId = measurementId,
                    mnoContainerId = mnoContainer.id,
                    containerTypeId = mnoContainer.type.id,
                    isUnique = false,
                    draftState = DraftState.ADDED
                )
            }
    }

    private fun observeContainerWasteTypes(container: SeparateContainerComposite): Rx3Observable<Effect> {
        draftHolder.initWasteTypeList(mapOf(container.localId to container.wasteTypes.toSet()))
        return draftHolder.observeContainerWasteTypeDrafts(container.localId)
            .map(Effect::WasteTypesUpdated)
    }

    private fun updateField(
        currentState: SeparateContainerComposite,
        dataType: ContainerDataType,
        value: String
    ): Observable<Effect> {
        val updatedDraft = when (dataType) {
            ContainerName -> currentState.copy(uniqueName = value)
            ContainerVolume -> currentState.copy(uniqueVolume = value.floatValue())
            else -> currentState
        }
        return Observable.just(Effect.DataInitialized(updatedDraft))
    }

    private fun saveData(container: SeparateContainerComposite): Observable<Effect> {
        val saveContainerCompletable = when (container.draftState) {
            DraftState.ADDED -> {
                measurementRepository.updateSeparateContainers(listOf(container))
            }
            DraftState.IDLE -> Completable.complete()
            DraftState.DELETED -> error("Unreachable condition")
        }
            .doOnComplete {
                exitAndClearTmpValues(
                    containerId = container.localId,
                    onExitFunc = { draftHolder.saveSeparateContainerDraft(container) }
                )
            }

        return saveContainerCompletable.withDataUpdateProgressState()
    }

    private fun deleteData(containerLocalId: Long): Observable<Effect> {
        return measurementRepository.setSeparateContainerDeleted(containerLocalId)
            .doOnComplete {
                exitAndClearTmpValues(
                    containerId = containerLocalId,
                    onExitFunc = { draftHolder.deleteSeparateContainerDraft(containerLocalId) }
                )
            }
            .withDataUpdateProgressState()
    }

    private fun clearUnfinishedData(state: State): Observable<Effect> {
        val container = state.separateContainer

        val rollbackModifiedContainerCompletable = when (container.draftState) {
            DraftState.ADDED -> {
                if (draftHolder.isContainerAddedToMeasurement(container.localId)) {
                    Completable.complete()
                } else {
                    measurementRepository.deleteSeparateWasteContainer(container.localId)
                }
            }
            DraftState.IDLE -> Completable.complete()
            DraftState.DELETED -> error("Unreachable condition")
        }
            .doOnComplete { exitAndClearTmpValues(containerId = container.localId) }

        return rollbackModifiedContainerCompletable
            .withDataUpdateProgressState(successEffect = Effect.DataCleared)
    }

    private fun stopObservingContainer() = interruptSignal.onNext(Unit)

    private fun exitAndClearTmpValues(containerId: Long, onExitFunc: () -> Unit = {}) {
        onExitFunc.invoke()
        draftHolder.clearContainerWasteTypeDrafts(containerId)
    }

    private fun Completable.withDataUpdateProgressState(
        successEffect: Effect = Effect.DataUpdated,
        loadingEffect: Effect = Effect.DataLoading,
        errorEffect: (Throwable) -> Effect = Effect::DataUpdateFailed
    ): Observable<Effect> {
        return andThen(Rx3Observable.just(successEffect))
            .startWithItem(loadingEffect)
            .onErrorReturn { errorEffect(it) }
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }
}
