package ru.ktsstudio.reo.domain.measurement.morphology.item_info

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup

data class MorphologyItemDraft(
    val selectedWasteGroup: WasteGroup?,
    val selectedWasteSubgroup: WasteSubgroup?,
    val wasteGroups: List<WasteGroup>,
    val wasteGroupIdToSubgroupsMap: Map<String, List<WasteSubgroup>>,
    val dailyGainWeight: Float?,
    val dailyGainVolume: Float?,
    val draftState: DraftState
) {
    companion object {
        fun getEmptyDraft(): MorphologyItemDraft {
            return MorphologyItemDraft(
                selectedWasteGroup = null,
                selectedWasteSubgroup = null,
                wasteGroups = emptyList(),
                wasteGroupIdToSubgroupsMap = emptyMap(),
                dailyGainWeight = null,
                dailyGainVolume = null,
                draftState = DraftState.ADDED
            )
        }
    }
}
