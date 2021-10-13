package ru.ktsstudio.reo.presentation.measurement.morphology.item_info

import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
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
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.MorphologyItemDraft
import ru.ktsstudio.reo.presentation.measurement.edit_waste_type.WasteGroupUi
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty

/**
 * Created by Igor Park on 21/10/2020.
 */
class EditMorphologyItemUiStateTransformer(
    private val resources: ResourceManager
) : (Pair<EditMorphologyItemFeature.State, FormFeature.State>) -> EditMorphologyItemUiState {

    override fun invoke(
        statePair: Pair<EditMorphologyItemFeature.State, FormFeature.State>
    ): EditMorphologyItemUiState {
        val state = statePair.first
        val form = statePair.second
        with(state) {
            val containerFields = map(form, morphologyItemDraft, state.requiredDataTypes)
            return EditMorphologyItemUiState(
                fields = containerFields,
                isReady = containerFields.filterIsInstance<DataField>()
                    .validate() && form.formState.isCorrect,
                isLoading = isLoading,
                error = error
            )
        }
    }

    private fun map(
        form: FormFeature.State,
        item: MorphologyItemDraft,
        requiredDataTypes: Set<ContainerDataType>
    ): List<Any> {
        val title = listOf(
            TitleItem(resources.getString(R.string.edit_mixed_container_fill_in_hint))
        )

        val wasteGroups = listOfNotNull(
            ContainerField.Selector(
                dataType = ContainerDataType.WasteGroup,
                dropDownItems = item.wasteGroups.map {
                    WasteGroupUi(
                        id = it.id,
                        title = it.name,
                        isSelected = item.selectedWasteGroup?.id == it.id
                    )
                },
                title = R.string.edit_waste_group_title,
                hint = R.string.edit_waste_type_add_waste_group_hint,
                value = item.selectedWasteGroup?.name,
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteGroup
                )
            ).takeIf { it.dropDownItems.isNotEmpty() },
            ContainerField.Selector(
                dataType = ContainerDataType.WasteSubgroup,
                dropDownItems = item.wasteGroupIdToSubgroupsMap[item.selectedWasteGroup?.id]
                    .orEmpty()
                    .map {
                        WasteGroupUi(
                            id = it.id,
                            title = it.name,
                            isSelected = item.selectedWasteSubgroup?.id == it.id
                        )
                    },
                title = R.string.edit_waste_subgroup_title,
                hint = R.string.edit_waste_type_add_waste_subgroup_hint,
                value = item.selectedWasteSubgroup?.name,
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteSubgroup
                )
            ).takeIf { it.dropDownItems.isNotEmpty() },
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
                dataType = ContainerDataType.WasteWeightDaily,
                title = R.string.edit_mixed_container_waste_daily_weight_title,
                hint = R.string.edit_mixed_container_weight_netto_hint,
                error = form.getFieldError(ContainerDataType.WasteWeightDaily),
                value = item.dailyGainWeight.stringValue(),
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteWeightDaily
                )
            )
        )

        return wasteGroups.takeIfNotEmpty()
            ?.let { title + wasteGroups }
            .orEmpty()
    }
}
