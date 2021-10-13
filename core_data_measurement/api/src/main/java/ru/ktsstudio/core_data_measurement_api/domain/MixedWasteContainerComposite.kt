package ru.ktsstudio.core_data_measurement_api.domain

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer

data class MixedWasteContainerComposite(
    val localId: Long,
    val isUnique: Boolean,
    val containerType: ContainerType,
    val mnoContainer: MnoContainer?,
    val uniqueName: String?,
    val uniqueVolume: Float?,
    val containerFullness: Float?,
    val wasteVolume: Float,
    val dailyGainVolume: Float,
    val netWeight: Float,
    val dailyGainNetWeight: Float,
    val emptyContainerWeight: Float?,
    val filledContainerWeight: Float?,
    val draftState: DraftState
) {
    fun getName(): String {
        return uniqueName.takeIf { isUnique }
            ?: mnoContainer?.name.orEmpty()
    }

    fun getVolume(): Float {
        return uniqueVolume.takeIf { isUnique }
            ?: mnoContainer?.volume
            ?: 0F
    }
}
