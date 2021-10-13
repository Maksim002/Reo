package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteType
import javax.inject.Inject

/**
 * Created by Igor Park on 15/10/2020.
 */
class ContainerWasteTypeDbMapper @Inject constructor() : Mapper2<
    ContainerWasteType,
    Long,
    LocalContainerWasteType
    > {
    override fun map(item1: ContainerWasteType, item2: Long): LocalContainerWasteType =
        with(item1) {
            return LocalContainerWasteType(
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
                draftState = draftState
            )
        }
}