package ru.ktsstudio.reo.domain.measurement.edit_waste_type

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategory

data class WasteTypeDraft(
    val categoryOptions: List<WasteCategory>,
    val selectedCategoryId: String?,
    val selectedCategoryName: String?,
    val nameOtherCategory: String?,
    val containerVolume: Float?,
    val containerFullness: Float?,
    val wasteVolume: Float?,
    val dailyGainNetWeight: Float?,
    val netWeight: Float?,
    val dailyGainVolume: Float?,
    val isOtherCategory: Boolean,
    val draftState: DraftState
) {
    companion object {
        fun getEmptyDraft(): WasteTypeDraft {
            return WasteTypeDraft(
                categoryOptions = emptyList(),
                selectedCategoryId = null,
                selectedCategoryName = null,
                nameOtherCategory = null,
                containerVolume = null,
                containerFullness = null,
                wasteVolume = null,
                dailyGainNetWeight = null,
                netWeight = null,
                dailyGainVolume = null,
                isOtherCategory = false,
                draftState = DraftState.ADDED
            )
        }
    }
}
