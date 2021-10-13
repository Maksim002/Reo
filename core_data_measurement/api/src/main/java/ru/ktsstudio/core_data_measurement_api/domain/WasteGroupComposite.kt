package ru.ktsstudio.core_data_measurement_api.domain

import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup

data class WasteGroupComposite(
    val group: WasteGroup,
    val subgroups: List<WasteSubgroup>
)