package ru.ktsstudio.core_data_measurement_api.domain

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer

/**
 * Created by Igor Park on 29/10/2020.
 */
data class SeparateContainerComposite(
    val localId: Long,
    val isUnique: Boolean,
    val containerType: ContainerType?,
    val mnoContainer: MnoContainer?,
    val uniqueName: String?,
    val uniqueVolume: Float?,
    val wasteTypes: List<ContainerWasteType>,
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
