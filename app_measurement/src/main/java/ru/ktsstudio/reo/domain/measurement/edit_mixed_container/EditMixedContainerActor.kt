package ru.ktsstudio.reo.domain.measurement.edit_mixed_container

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.common.utils.rx.Rx3Maybe
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.errorIfEmpty
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.reo.domain.measurement.common.MixedContainerDraftHolder
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature.Effect
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature.State
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature.Wish
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.ContainerEmptyWeight
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.ContainerFilledWeight
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.ContainerFullness
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.ContainerName
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.ContainerVolume
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.WasteVolume
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.WasteVolumeDaily
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.WasteWeightDaily
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.MixedWasteContainerDraft
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm

/**
 * Created by Igor Park on 19/10/2020.
 */
class EditMixedContainerActor(
    private val measurementRepository: MeasurementRepository,
    private val mnoRepository: MnoRepository,
    private val mixedContainerToDraftMapper: Mapper<MixedWasteContainerComposite, MixedWasteContainerDraft>,
    private val draftToMixedContainerMapper: Mapper2<MixedWasteContainerDraft, Long, MixedWasteContainerComposite>,
    private val draftToMeasurementFormMapper: Mapper<MixedWasteContainerDraft, MeasurementForm>,
    private val schedulers: SchedulerProvider,
    private val draftHolder: MixedContainerDraftHolder
) : Actor<State, Wish, Effect> {

    private val interruptSignal = Rx2PublishSubject.create<Unit>()

    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        return when (action) {
            is Wish.InitData -> initData(action)
            is Wish.UpdateField -> updateField(
                currentDraft = state.containerDraft,
                dataType = action.dataType,
                value = action.value
            )
            is Wish.SaveData -> {
                saveData(action.measurementId, action.containerId, state.containerDraft)
            }
            is Wish.DeleteData -> deleteData(action.containerId)
        }
    }

    private fun initData(initAction: Wish.InitData): Observable<Effect> {
        val containerExist = initAction.containerId != null
        val mnoContainerSelected = initAction.mnoContainerId != null
        val typeSelected = initAction.containerTypeId != null

        interruptSignal.onNext(Unit)

        return when {
            containerExist -> getExistingContainer(initAction.containerId!!)
            mnoContainerSelected -> prepareContainerDraftWithName(initAction.mnoContainerId!!)
            typeSelected -> prepareContainerDraftWithType(initAction.containerTypeId!!)
            else -> error("Unreachable condition")
        }
            .withProgressState()
            .takeUntil(interruptSignal)
    }

    private fun updateField(
        currentDraft: MixedWasteContainerDraft,
        dataType: ContainerDataType,
        value: String
    ): Observable<Effect> {
        val updatedDraft = when (dataType) {
            ContainerName -> currentDraft.copy(uniqueName = value)
            ContainerVolume -> currentDraft.copy(uniqueVolume = value.floatValue())
            ContainerEmptyWeight -> currentDraft.copy(emptyContainerWeight = value.floatValue())
            ContainerFilledWeight -> currentDraft.copy(filledContainerWeight = value.floatValue())
            ContainerFullness -> currentDraft.copy(containerFullness = value.floatValue())
            ContainerDataType.WasteWeight -> currentDraft.copy(netWeight = value.floatValue())
            WasteVolume -> currentDraft.copy(wasteVolume = value.floatValue())
            WasteVolumeDaily -> currentDraft.copy(dailyGainVolume = value.floatValue())
            WasteWeightDaily -> currentDraft.copy(dailyGainNetWeight = value.floatValue())
            else -> currentDraft
        }
        return Observable.just(
            Effect.DataInitialized(updatedDraft),
            Effect.FormChanged(draftToMeasurementFormMapper.map(updatedDraft))
        )
    }

    private fun saveData(
        measurementId: Long,
        containerId: Long?,
        containerDraft: MixedWasteContainerDraft
    ): Observable<Effect> {
        return if (containerId == null || containerDraft.draftState == DraftState.ADDED) {
            measurementRepository.saveMixedContainer(
                measurementId = measurementId,
                container = draftToMixedContainerMapper.map(containerDraft, 0L)
            )
        } else {
            Completable.fromCallable {
                draftHolder.saveMixedContainerDraft(
                    draftToMixedContainerMapper.map(containerDraft, containerId)
                )
            }
        }
            .withDataUpdateProgressState()
    }

    private fun deleteData(containerLocalId: Long): Observable<Effect> {
        return measurementRepository.setMixedContainerDeleted(containerLocalId)
            .withDataUpdateProgressState()
    }

    private fun getExistingContainer(containerId: Long): Rx3Observable<MixedWasteContainerDraft> {
        val mixedContainerMaybe = draftHolder.getMixedContainerDraft(containerId)
            ?.let { Rx3Maybe.just(it) }
            ?: measurementRepository.getMixedWasteContainerById(containerId)
        return mixedContainerMaybe
            .map(mixedContainerToDraftMapper::map)
            .toObservable()
    }

    private fun prepareContainerDraftWithName(mnoContainerId: String): Rx3Observable<MixedWasteContainerDraft> {
        return mnoRepository.getMnoContainerById(mnoContainerId)
            .errorIfEmpty()
            .toObservable()
            .map { mnoContainer ->
                MixedWasteContainerDraft.getEmptyDraft()
                    .copy(
                        containerType = mnoContainer.type,
                        mnoContainer = mnoContainer,
                        draftState = DraftState.ADDED,
                        isUnique = false
                    )
            }
    }

    private fun prepareContainerDraftWithType(containerTypeId: String): Rx3Observable<MixedWasteContainerDraft> {
        return measurementRepository.getContainerTypeById(containerTypeId)
            .errorIfEmpty()
            .toObservable()
            .map { containerType ->
                MixedWasteContainerDraft.getEmptyDraft()
                    .copy(
                        containerType = containerType,
                        draftState = DraftState.ADDED,
                        isUnique = true
                    )
            }
    }

    private fun Rx3Observable<MixedWasteContainerDraft>.withProgressState(): Observable<Effect> {
        return map(Effect::DataInitialized)
            .flatMap {
                Rx3Observable.just(
                    it,
                    Effect.FormChanged(draftToMeasurementFormMapper.map(it.containerDraft))
                )
            }
            .startWithItem(Effect.DataLoading)
            .onErrorReturn(Effect::DataLoadError)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun Completable.withDataUpdateProgressState(): Observable<Effect> {
        return andThen(Rx3Observable.just<Effect>(Effect.DataUpdateCompleted))
            .startWithItem(Effect.DataUpdating)
            .onErrorReturn(Effect::DataUpdateFailed)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }
}
