package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.SeparateWasteContainerLocalId
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteType
import ru.ktsstudio.utilities.extensions.orDefault
import javax.inject.Inject
import kotlin.math.sign

class SeparateContainerWasteTypeDbMapper @Inject constructor() :
    Mapper2<ContainerWasteType, SeparateWasteContainerLocalId, LocalContainerWasteType> {

    override fun map(
        item1: ContainerWasteType,
        item2: SeparateWasteContainerLocalId
    ): LocalContainerWasteType = with(item1) {
        LocalContainerWasteType(
            localId = localId,
            containerLocalId = item2,
            categoryId = categoryId,
            categoryName = categoryName,
            nameOtherCategory = nameOtherCategory,
            containerVolume = containerVolume,
            containerFullness = containerFullness,
            wasteVolume = wasteVolume,
            dailyGainVolume = dailyGainVolume,
            netWeight = netWeight,
            dailyGainNetWeight = dailyGainNetWeight,
            draftState = DraftState.IDLE
        )
    }
}