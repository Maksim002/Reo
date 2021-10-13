package ru.ktsstudio.reo.presentation.measurement.edit_separate_container

import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.small.SmallTitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.stringValue
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerField
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.DataField
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.validate
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature

/**
 * Created by Igor Park on 21/10/2020.
 */
class EditSeparateContainerUiStateTransformer(
    private val resources: ResourceManager
) : (EditSeparateContainerFeature.State) -> EditSeparateContainerUiState {

    override fun invoke(state: EditSeparateContainerFeature.State): EditSeparateContainerUiState =
        with(state) {
            val containerFields = map(state.separateContainer, state.requiredDataTypes)
            val wasteTypesTitle = SmallTitleItem(
                text = resources.getString(R.string.edit_separate_container_waste_types)
            )
            val wasteTypes = state.separateContainer.wasteTypes.map {
                WasteTypeUiItem(
                    id = it.localId,
                    categoryName = it.categoryName,
                    otherCategoryName = it.nameOtherCategory
                )
            }
            val addWasteTypeButton = AddEntityItem(
                text = resources.getString(R.string.edit_separate_container_add_waste_types),
                icon = R.drawable.ic_plus,
                qualifier = Unit
            )

            val wasteTypesBlock = listOf(wasteTypesTitle) + wasteTypes + listOf(addWasteTypeButton)

            return EditSeparateContainerUiState(
                containerId = state.separateContainer.localId,
                containerFields = containerFields + wasteTypesBlock,
                isReady = separateContainer.wasteTypes.isNotEmpty() &&
                    containerFields.filterIsInstance<DataField>()
                        .validate(),
                isLoading = isLoading,
                error = error
            )
        }

    private fun map(
        item: SeparateContainerComposite,
        requiredDataTypes: Set<ContainerDataType>
    ): List<Any> {
        return listOf(
            TitleItem(
                resources.getString(
                    R.string.edit_mixed_container_fill_in_hint
                )
            ),
            LabeledValueItem(
                label = resources.getString(R.string.measurement_container_type_title),
                value = item.mnoContainer?.type?.name
                    ?: item.containerType?.name.orEmpty()
            ),
            if (item.isUnique) {
                ContainerField.OpenField(
                    dataType = ContainerDataType.ContainerName,
                    title = R.string.edit_mixed_container_container_name_title,
                    hint = R.string.edit_mixed_container_container_name_hint,
                    value = item.uniqueName,
                    isRequired = requiredDataTypes.contains(ContainerDataType.ContainerName)
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
                    isRequired = requiredDataTypes.contains(ContainerDataType.ContainerVolume)
                )
            } else {
                LabeledValueItem(
                    label = resources.getString(R.string.edit_mixed_container_volume_title),
                    value = item.mnoContainer?.volume.stringValue()
                )
            }
        )
    }
}
