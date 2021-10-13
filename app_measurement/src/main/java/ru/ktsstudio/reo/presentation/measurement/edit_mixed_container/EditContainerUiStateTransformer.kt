package ru.ktsstudio.reo.presentation.measurement.edit_mixed_container

import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.medium.MediumTitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.form.getFieldError
import ru.ktsstudio.common.utils.form.isCorrect
import ru.ktsstudio.common.utils.stringValue
import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.DataField
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.MixedWasteContainerDraft
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.validate

/**
 * Created by Igor Park on 21/10/2020.
 */
class EditContainerUiStateTransformer(
    private val resources: ResourceManager
) : (Pair<EditMixedContainerFeature.State, FormFeature.State>) -> EditMixedContainerUiState {
    override fun invoke(
        statePair: Pair<EditMixedContainerFeature.State, FormFeature.State>
    ): EditMixedContainerUiState {

        val state = statePair.first
        val form = statePair.second

        with(state) {
            val containerFields = map(form, containerDraft, state.requiredDataTypes)
            return EditMixedContainerUiState(
                containerFields = containerFields,
                isReady = containerFields.filterIsInstance<DataField>()
                    .validate() && form.formState.isCorrect,
                isUpdating = isUpdating,
                isLoading = isLoading,
                error = error
            )
        }
    }

    private fun map(
        form: FormFeature.State,
        item: MixedWasteContainerDraft,
        requiredDataTypes: Set<ContainerDataType>
    ): List<Any> {
        return listOf(
            TitleItem(
                resources.getString(R.string.edit_mixed_container_fill_in_hint)
            ),
            LabeledValueItem(
                label = resources.getString(R.string.measurement_container_type_title),
                value = item.mnoContainer?.type?.name ?: item.containerType?.name.orEmpty()
            ),
            if (item.isUnique) {
                ContainerField.OpenField(
                    dataType = ContainerDataType.ContainerName,
                    title = R.string.edit_mixed_container_container_name_title,
                    hint = R.string.edit_mixed_container_container_name_hint,
                    value = item.uniqueName,
                    error = null,
                    isRequired = requiredDataTypes.contains(
                        ContainerDataType.ContainerName
                    )
                )
            } else {
                LabeledValueItem(
                    label = resources.getString(R.string.edit_mixed_container_container_name_title),
                    value = item.mnoContainer?.name.orEmpty()
                )
            },
            if (item.isUnique) {
                ContainerField.OpenField(
                    dataType = ContainerDataType.ContainerVolume,
                    title = R.string.edit_mixed_container_volume_title,
                    hint = R.string.edit_mixed_container_volume_hint,
                    value = item.uniqueVolume.stringValue(),
                    isRequired = requiredDataTypes.contains(
                        ContainerDataType.ContainerVolume
                    )
                )
            } else {
                LabeledValueItem(
                    label = resources.getString(R.string.edit_mixed_container_volume_title),
                    value = item.mnoContainer?.volume.stringValue()
                )
            },
            MediumTitleItem(resources.getString(R.string.edit_mixed_container_waste_parameters)),
            ContainerField.OpenField(
                dataType = ContainerDataType.ContainerFullness,
                title = R.string.edit_mixed_container_fullness_title,
                hint = R.string.edit_mixed_container_fullness_hint,
                value = item.containerFullness.stringValue(),
                error = null,
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.ContainerFullness
                )
            ),
            ContainerField.OpenField(
                dataType = ContainerDataType.ContainerEmptyWeight,
                title = R.string.edit_mixed_container_empty_weight_title,
                hint = R.string.edit_mixed_container_weight_hint,
                value = item.emptyContainerWeight.stringValue(),
                error = null,
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.ContainerEmptyWeight
                )
            ),
            ContainerField.OpenField(
                dataType = ContainerDataType.ContainerFilledWeight,
                title = R.string.edit_mixed_container_filled_weight_title,
                hint = R.string.edit_mixed_container_weight_hint,
                value = item.filledContainerWeight.stringValue(),
                error = null,
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.ContainerFilledWeight
                )
            ),
            ContainerField.OpenField(
                dataType = ContainerDataType.WasteWeight,
                title = R.string.edit_mixed_container_waste_weight_title,
                hint = R.string.edit_mixed_container_weight_hint,
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
                value = item.dailyGainNetWeight.stringValue(),
                error = form.getFieldError(ContainerDataType.WasteWeightDaily),
                isRequired = requiredDataTypes.contains(
                    ContainerDataType.WasteWeightDaily
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
            )
        )
    }
}
