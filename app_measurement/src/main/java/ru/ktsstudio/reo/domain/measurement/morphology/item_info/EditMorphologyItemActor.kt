package ru.ktsstudio.reo.domain.measurement.morphology.item_info

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.utils.checkValue
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.model.Morphology
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_api.domain.WasteGroupComposite
import ru.ktsstudio.reo.domain.measurement.common.MorphologyDraftHolder
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature.Effect
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature.State
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature.Wish
import ru.ktsstudio.utilities.extensions.orDefault
import ru.ktsstudio.utilities.extensions.requireNotNull

/**
 * Created by Igor Park on 19/10/2020.
 */
class EditMorphologyItemActor(
    private val measurementRepository: MeasurementRepository,
    private val morphologyItemToDraftMapper: Mapper<MorphologyItem, MorphologyItemDraft>,
    private val draftToMorphologyItemMapper: Mapper2<MorphologyItemDraft, Long, MorphologyItem>,
    private val draftHolder: MorphologyDraftHolder,
    private val schedulers: SchedulerProvider
) : Actor<State, Wish, Effect> {

    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        return when (action) {
            is Wish.InitData -> prepareMorphologyItemDraftWithCategories(action.categoryId)
            is Wish.UpdateField -> updateField(
                morphologyItemDraft = state.morphologyItemDraft,
                dataType = action.dataType,
                value = action.value
            )
            is Wish.SaveData -> saveData(
                action.measurementId,
                action.categoryId,
                state.morphologyItemDraft
            )
            is Wish.DeleteData -> deleteData(action.categoryId)
        }
    }

    private fun updateField(
        morphologyItemDraft: MorphologyItemDraft,
        dataType: ContainerDataType,
        value: String
    ): Observable<Effect> {
        val updatedDraft = when (dataType) {
            ContainerDataType.WasteGroup -> {
                morphologyItemDraft.wasteGroups
                    .find { it.id == value }
                    ?.let { selectedGroup ->
                        morphologyItemDraft.copy(
                            selectedWasteGroup = selectedGroup,
                            selectedWasteSubgroup = morphologyItemDraft.wasteGroupIdToSubgroupsMap[selectedGroup.id]
                                ?.contains(morphologyItemDraft.selectedWasteSubgroup)
                                ?.let { inGroup ->
                                    morphologyItemDraft.selectedWasteSubgroup
                                        ?.takeIf { inGroup }
                                }
                        )
                    }
            }
            ContainerDataType.WasteSubgroup -> {
                val selectedWasteGroupId = morphologyItemDraft.selectedWasteGroup?.id
                morphologyItemDraft.wasteGroupIdToSubgroupsMap[selectedWasteGroupId]
                    ?.find { subgroup -> subgroup.id == value }
                    ?.let { selectedSubgroup ->
                        morphologyItemDraft.copy(selectedWasteSubgroup = selectedSubgroup)
                    }
            }
            ContainerDataType.WasteVolumeDaily -> morphologyItemDraft.copy(dailyGainVolume = value.floatValue())
            ContainerDataType.WasteWeightDaily -> morphologyItemDraft.copy(dailyGainWeight = value.floatValue())
            else -> morphologyItemDraft
        }
        return Observable.just(Effect.DataInitialized(updatedDraft ?: morphologyItemDraft))
    }

    private fun saveData(
        measurementId: Long,
        morphologyId: Long?,
        morphologyItemDraft: MorphologyItemDraft
    ): Observable<Effect> {
        return when (morphologyItemDraft.draftState) {
            DraftState.ADDED -> {
                measurementRepository.updateMorphology(
                    measurementId = measurementId,
                    morphology = Morphology(
                        localId = morphologyId.orDefault(0L),
                        groupId = checkValue(
                            morphologyItemDraft.selectedWasteGroup?.id,
                            "morphologyItemDraft.selectedWasteGroupId"
                        ),
                        subGroupId = morphologyItemDraft.selectedWasteSubgroup?.id,
                        dailyGainVolume = checkValue(
                            morphologyItemDraft.dailyGainVolume,
                            "morphologyItemDraft.dailyGainVolume"
                        ),
                        dailyGainWeight = checkValue(
                            morphologyItemDraft.dailyGainWeight,
                            "morphologyItemDraft.dailyGainWeight"
                        ),
                        draftState = morphologyItemDraft.draftState
                    )
                )
            }
            DraftState.IDLE -> {
                Completable.fromCallable {
                    draftHolder.saveMorphologyItem(
                        draftToMorphologyItemMapper.map(
                            morphologyItemDraft,
                            morphologyId.requireNotNull()
                        )
                    )
                }
            }
            DraftState.DELETED -> error("Unreachable condition")
        }
            .withDataUpdateProgressState()
    }

    private fun deleteData(categoryId: Long): Observable<Effect> {
        return measurementRepository.setMorphologyItemDeleted(categoryId)
            .withDataUpdateProgressState()
    }

    private fun prepareMorphologyItemDraftWithCategories(localId: Long?): Observable<Effect> {
        val morphologyItemDraftSingle = if (localId != null) {
            val prepareDraftSingle = draftHolder.getMorphologyItemDraft(localId)
                ?.let { Single.just(it) }
                ?: measurementRepository.getMorphologyById(localId)
                    .toSingle()
            prepareDraftSingle.map(morphologyItemToDraftMapper::map)
        } else {
            Single.just(MorphologyItemDraft.getEmptyDraft())
        }

        return Single.zip(
            morphologyItemDraftSingle,
            measurementRepository.getCompositeWasteGroups(),
            BiFunction { morphologyItemDraft: MorphologyItemDraft, compositeWasteGroups: List<WasteGroupComposite> ->
                morphologyItemDraft.copy(
                    wasteGroups = compositeWasteGroups.map { it.group },
                    wasteGroupIdToSubgroupsMap = compositeWasteGroups.associateBy(
                        keySelector = { it.group.id },
                        valueTransform = { it.subgroups }
                    )
                )
            }
        )
            .toObservable()
            .withProgressState()
    }

    private fun Rx3Observable<MorphologyItemDraft>.withProgressState(): Observable<Effect> {
        return map<Effect>(Effect::DataInitialized)
            .startWithItem(Effect.DataLoading)
            .onErrorReturn(Effect::DataLoadError)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun Completable.withDataUpdateProgressState(): Observable<Effect> {
        return andThen(Rx3Observable.just<Effect>(Effect.DataUpdateCompleted))
            .startWithItem(Effect.DataLoading)
            .onErrorReturn(Effect::DataUpdateFailed)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }
}
