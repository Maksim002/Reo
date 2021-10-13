package ru.ktsstudio.core_data_measurement_api.domain

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup

data class MorphologyItem(
    val localId: Long,
    val group: WasteGroup,
    val subgroup: WasteSubgroup?,
    val dailyGainWeight: Float,
    val dailyGainVolume: Float,
    val draftState: DraftState
)