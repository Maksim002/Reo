package ru.ktsstudio.reo.presentation.measurement.add_container

import ru.ktsstudio.common.presentation.filter.UiFilterItem
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType

/**
 * Created by Igor Park on 19/10/2020.
 */
data class ContainerTypeUi(
    val containerType: ContainerType,
    override val isSelected: Boolean
) : UiFilterItem {
    override val id: String = containerType.id
    override val title: String = containerType.name
}
