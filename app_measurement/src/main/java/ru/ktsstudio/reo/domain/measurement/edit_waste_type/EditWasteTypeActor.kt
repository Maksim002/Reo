package ru.ktsstudio.reo.domain.measurement.edit_waste_type

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.id.IdGenerator
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.Rx3Single
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategory
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategoryConstants
import ru.ktsstudio.reo.domain.measurement.common.WasteTypeDraftHolder
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.ContainerFullness
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.ContainerVolume
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.WasteTypeCategory
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.WasteTypeOtherCategoryName
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.WasteVolume
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.WasteVolumeDaily
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.WasteWeight
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType.WasteWeightDaily
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature.Effect
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature.State
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature.Wish
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm

/**
 * Created by Igor Park on 19/10/2020.
 */
class EditWasteTypeActor(
    private val measurementRepository: MeasurementRepository,
    private val wasteTypeToDraftMapper: Mapper<ContainerWasteType, WasteTypeDraft>,
    private val draftToWasteTypeMapper: Mapper2<WasteTypeDraft, String, ContainerWasteType>,
    private val draftToFormMapper: Mapper<WasteTypeDraft, MeasurementForm>,
    private val schedulers: SchedulerProvider,
    private val draftHolder: WasteTypeDraftHolder,
    private val idGenerator: IdGenerator
) : Actor<State, Wish, Effect> {

    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        return when (action) {
            is Wish.InitData -> prepareWasteTypeDraftWithCategories(action)
            is Wish.UpdateField -> updateField(
                wasteTypeDraft = state.wasteTypeDraft,
                dataType = action.dataType,
                value = action.value
            )
            is Wish.SaveData -> saveData(
                action.containerId,
                action.wasteTypeId,
                state.wasteTypeDraft
            )
            is Wish.DeleteData -> deleteData(action.containerId, action.wasteTypeId)
        }
    }

    private fun prepareWasteTypeDraftWithCategories(action: Wish.InitData): Observable<Effect> {
        val wasteTypeDraftSingle = if (action.wasteTypeId != null) {
            val getWasteTypeSingle = draftHolder.getContainerWasteTypeDraft(action.wasteTypeId)
                ?.let { Rx3Single.just(it) }
                ?: error("no waste type in the draft with id = ${action.wasteTypeId}!!!")
            getWasteTypeSingle.map(wasteTypeToDraftMapper::map)
        } else {
            Single.just(WasteTypeDraft.getEmptyDraft())
        }

        return Single.zip(
            wasteTypeDraftSingle,
            measurementRepository.getWasteCategories(),
            BiFunction { wasteTypeDraft: WasteTypeDraft, wasteCategories: List<WasteCategory> ->
                wasteTypeDraft.copy(categoryOptions = wasteCategories)
            }
        )
            .toObservable()
            .withProgressState()
    }

    private fun updateField(
        wasteTypeDraft: WasteTypeDraft,
        dataType: ContainerDataType,
        value: String
    ): Observable<Effect> {
        val updatedDraft = when (dataType) {
            WasteTypeCategory -> {
                wasteTypeDraft.categoryOptions
                    .find { it.id == value }
                    ?.let { selectedCategory ->
                        wasteTypeDraft.copy(
                            selectedCategoryId = selectedCategory.id,
                            selectedCategoryName = selectedCategory.name,
                            isOtherCategory = selectedCategory.id == WasteCategoryConstants.OTHER_CATEGORY.id
                        )
                    }
                    ?: wasteTypeDraft
            }
            ContainerVolume -> wasteTypeDraft.copy(containerVolume = value.floatValue())
            WasteTypeOtherCategoryName -> wasteTypeDraft.copy(nameOtherCategory = value)
            ContainerFullness -> wasteTypeDraft.copy(containerFullness = value.floatValue())
            WasteVolume -> wasteTypeDraft.copy(wasteVolume = value.floatValue())
            WasteVolumeDaily -> wasteTypeDraft.copy(dailyGainVolume = value.floatValue())
            WasteWeight -> wasteTypeDraft.copy(netWeight = value.floatValue())
            WasteWeightDaily -> wasteTypeDraft.copy(dailyGainNetWeight = value.floatValue())
            else -> wasteTypeDraft
        }
        return Observable.just(
            Effect.DataInitialized(updatedDraft),
            Effect.FormChanged(draftToFormMapper.map(updatedDraft))
        )
    }

    private fun saveData(
        containerId: Long,
        wasteTypeId: String?,
        wasteTypeDraft: WasteTypeDraft
    ): Observable<Effect> {
        return Completable.fromCallable {
            draftHolder.saveContainerWasteTypeDraft(
                containerId = containerId,
                wasteType = draftToWasteTypeMapper.map(
                    wasteTypeDraft,
                    wasteTypeId ?: idGenerator.generateStringId()
                )
            )
        }
            .withDataUpdateProgressState()
    }

    private fun deleteData(containerId: Long, wasteTypeId: String): Observable<Effect> {
        return measurementRepository.setWasteTypeDeleted(wasteTypeId)
            .doOnComplete { draftHolder.deleteContainerWasteTypeDraft(containerId, wasteTypeId) }
            .withDataUpdateProgressState()
    }

    private fun Rx3Observable<WasteTypeDraft>.withProgressState(): Observable<Effect> {
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
