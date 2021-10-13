package ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer

data class MixedWasteContainerDraft(
    val isUnique: Boolean,
    val containerType: ContainerType?,
    val mnoContainer: MnoContainer?,
    val uniqueName: String?,
    val uniqueVolume: Float?,
    val containerFullness: Float?,
    val emptyContainerWeight: Float?,
    val filledContainerWeight: Float?,
    val netWeight: Float?,
    val dailyGainNetWeight: Float?,
    val wasteVolume: Float?,
    val dailyGainVolume: Float?,
    val draftState: DraftState
) {
    companion object {
        fun getEmptyDraft(): MixedWasteContainerDraft {
            return MixedWasteContainerDraft(
                isUnique = false,
                containerType = null,
                mnoContainer = null,
                uniqueName = null,
                uniqueVolume = null,
                containerFullness = null,
                emptyContainerWeight = null,
                filledContainerWeight = null,
                netWeight = null,
                dailyGainNetWeight = null,
                wasteVolume = null,
                dailyGainVolume = null,
                draftState = DraftState.IDLE
            )
        }
    }
}
