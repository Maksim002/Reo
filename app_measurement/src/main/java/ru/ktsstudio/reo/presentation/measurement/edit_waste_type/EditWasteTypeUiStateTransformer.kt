package ru.ktsstudio.reo.presentation.measurement.edit_waste_type

import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.medium.MediumTitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.form.getFieldError
import ru.ktsstudio.common.utils.form.isCorrect
import ru.ktsstudio.common.utils.stringValue
import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.DataField
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.validate
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.WasteTypeDraft

/**
 * Created by Igor Park on 21/10/2020.
 */
class EditWasteTypeUiStateTransformer(
    private val resources: ResourceManager
) : (Pair<EditWasteTypeFeature.State, FormFeature.State>) -> EditWasteTypeUiState {
    override fun invoke(statePair: Pair<EditWasteTypeFeature.State, FormFeature.State>): EditWasteTypeUiState {
        val state = statePair.first
        val form = statePair.second
        with(state) {
            val containerFields = map(wasteTypeDraft, state.requiredDataTypes, form)
            return EditWasteTypeUiState(
                containerFields = containerFields,
                isReady = containerFields.filterIsInstance<DataField>()
                    .validate() && form.formState.isCorrect,
                isLoading = isLoading,
                error = error
            )
        }
    }

    private fun map(
        item: WasteTypeDraft,
        requiredDataTypes: Set<ContainerDataType>,
        form: FormFeature.State
    ): List<Any> {
        return listOfNotNull(
            TitleItem(resources.getString(R.string.edit_mixed_container_fill_in_hint)),
            ContainerField.Selector(
                dataType = ContainerDataType.WasteTypeCategory,
                dropDownItems = item.categoryOptions.map {
                    WasteGroupUi(
                        id = it.id,
                        title = it.name,
                        isSelected = item.selectedCategoryId == it.id
                    )
                },
                title = R.string.edit_waste_type_category_title,
                hint = R.string.edit_waste_type_add_waste_category_hint,
                value = item.selectedCategoryName,
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteTypeCategory
                )
            ),
            ContainerField.OpenField(
                dataType = ContainerDataType.WasteTypeOtherCategoryName,
                title = R.string.edit_waste_type_name_other_category_title,
                hint = R.string.edit_mixed_container_container_name_hint,
                error = null,
                value = item.nameOtherCategory,
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteTypeOtherCategoryName
                )
            ).takeIf { item.isOtherCategory },
            ContainerField.OpenField(
                dataType = ContainerDataType.ContainerVolume,
                title = R.string.edit_mixed_container_volume_title,
                hint = R.string.edit_mixed_container_volume_hint,
                error = null,
                value = item.containerVolume.stringValue(),
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.ContainerVolume
                )
            ),
            MediumTitleItem(resources.getString(R.string.edit_mixed_container_waste_parameters)),
            ContainerField.OpenField(
                dataType = ContainerDataType.ContainerFullness,
                title = R.string.edit_mixed_container_fullness_title,
                hint = R.string.edit_mixed_container_fullness_hint,
                error = null,
                value = item.containerFullness.stringValue(),
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.ContainerFullness
                )
            ),
            ContainerField.OpenField(
                dataType = ContainerDataType.WasteVolume,
                title = R.string.edit_mixed_container_waste_volume_title,
                hint = R.string.edit_mixed_container_waste_volume_hint,
                error = form.getFieldError(ContainerDataType.WasteVolume),
                value = item.wasteVolume.stringValue(),
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteVolume
                )
            ),
            ContainerField.OpenField(
                dataType = ContainerDataType.WasteVolumeDaily,
                title = R.string.edit_mixed_container_volume_daily_title,
                hint = R.string.edit_mixed_container_volume_daily_hint,
                error = form.getFieldError(ContainerDataType.WasteVolumeDaily),
                value = item.dailyGainVolume.stringValue(),
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteVolumeDaily
                )
            ),
            ContainerField.OpenField(
                dataType = ContainerDataType.WasteWeight,
                title = R.string.edit_mixed_container_waste_weight_title,
                hint = R.string.edit_mixed_container_weight_netto_hint,
                error = form.getFieldError(ContainerDataType.WasteWeight),
                value = item.netWeight.stringValue(),
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteWeight
                )
            ),
            ContainerField.OpenField(
                dataType = ContainerDataType.WasteWeightDaily,
                title = R.string.edit_mixed_container_waste_daily_weight_title,
                hint = R.string.edit_mixed_container_weight_netto_hint,
                error = form.getFieldError(ContainerDataType.WasteWeightDaily),
                value = item.dailyGainNetWeight.stringValue(),
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteWeightDaily
                )
            )
        )
    }
}
