package ru.ktsstudio.feature_mno_list.presentation.details

import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItem
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.core_data_measurement_api.data.model.Source
import ru.ktsstudio.core_data_measurement_api.data.model.SourceAddress
import ru.ktsstudio.feature_mno_list.R
import ru.ktsstudio.feature_mno_list.domain.details.MnoDetailsFeature
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 03.10.2020.
 */
internal class MnoUiStateTransformer @Inject constructor(
    private val resourceManager: ResourceManager
) : (MnoDetailsFeature.State) -> MnoDetailsUiState {
    override fun invoke(state: MnoDetailsFeature.State): MnoDetailsUiState {
        return MnoDetailsUiState(
            loading = state.loading,
            error = state.error,
            data = state.mno?.let { createAdapterItems(it, state.measurements) } ?: emptyList()
        )
    }

    private fun createAdapterItems(details: Mno, measurements: List<Measurement>): List<Any> {
        return getSourceItems(details.source) +
            getSourceAddressItems(details.sourceAddress) +
            getMeasurementItems(measurements) +
            getContainerItems(details.containers)
    }

    private fun getSourceItems(source: Source): List<Any> {
        return listOf(
            TitleItem(resourceManager.getString(R.string.mno_details_source_title)),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_name_label),
                value = source.name
            ),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_type_label),
                value = source.type
            ),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_category_label),
                value = source.category.name
            ),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_subcategory_label),
                value = source.subcategory
            ),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_unit_label),
                value = source.unit.type
            ),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_unit_count_label),
                value = source.unit.quantity.toString()
            ),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_alt_unit_label),
                value = source.altUnit.type
            ),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_alt_unit_count_label),
                value = source.altUnit.quantity.toString()
            )
        )
    }

    private fun getSourceAddressItems(address: SourceAddress): List<Any> {
        return listOfNotNull(
            TitleItem(resourceManager.getString(R.string.mno_details_source_address_title)),
            address.federalDistrict
                ?.let { federalDistrict ->
                    LabeledValueItem(
                        label = resourceManager.getString(R.string.mno_details_source_address_federal_label),
                        value = federalDistrict
                    )
                }
            ,
            address.region
                ?.let {region->
                    LabeledValueItem(
                        label = resourceManager.getString(R.string.mno_details_source_address_region_label),
                        value = region
                    )
                }
            ,
            address.municipalDistrict
                ?.let {municipalDistrict->
                    LabeledValueItem(
                        label = resourceManager.getString(R.string.mno_details_source_address_municipal_label),
                        value = municipalDistrict
                    )
                }
            ,
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_source_address_label),
                value = address.address
            )
        )
    }

    private fun getMeasurementItems(
        measurements: List<Measurement>
    ): List<Any> {
        return listOf(
            TitleItem(resourceManager.getString(R.string.mno_details_measurements_title)),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_measurements_count_label),
                value = measurements.size.toString()
            )
        )
    }

    private fun getContainerItems(
        containers: List<MnoContainer>
    ): List<Any> {
        return listOf(
            TitleItem(resourceManager.getString(R.string.mno_details_containers_title)),
            LabeledValueItem(
                label = resourceManager.getString(R.string.mno_details_containers_count_label),
                value = containers.size.toString()
            )
        ) + containers.flatMap { container: MnoContainer ->
            listOf(
                CardCornersItem(isTop = true),
                CardTitleItem(text = container.name),
                LabeledValueItem(
                    label = resourceManager.getString(R.string.mno_details_container_type_label),
                    value = container.type.name,
                    inCard = true
                ),
                LabeledValueItem(
                    label = resourceManager.getString(R.string.mno_details_container_volume_label),
                    value = resourceManager.getString(
                        R.string.mno_details_container_volume_value,
                        container.volume
                    ),
                    inCard = true
                ),
                LabeledValueItem(
                    label = resourceManager.getString(R.string.mno_details_container_schedule_label),
                    value = container.scheduleType,
                    inCard = true
                ),
                CardCornersItem(isTop = false)
            )
        }
    }
}
