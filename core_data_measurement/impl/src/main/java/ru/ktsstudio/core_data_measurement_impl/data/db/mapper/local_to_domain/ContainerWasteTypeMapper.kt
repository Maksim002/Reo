package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteTypeWithRelation
import javax.inject.Inject

/**
 * Created by Igor Park on 31/10/2020.
 */
class ContainerWasteTypeMapper @Inject constructor() :
    Mapper<LocalContainerWasteTypeWithRelation, ContainerWasteType> {
    override fun map(item: LocalContainerWasteTypeWithRelation): ContainerWasteType = with(item) {
        return ContainerWasteType(
            localId = wasteType.localId,
            categoryId = wasteType.categoryId,
            categoryName = wasteType.categoryName,
            nameOtherCategory = wasteType.nameOtherCategory,
            containerVolume = wasteType.containerVolume,
            containerFullness = wasteType.containerFullness,
            wasteVolume = wasteType.wasteVolume,
            dailyGainVolume = wasteType.dailyGainVolume,
            netWeight = wasteType.netWeight,
            dailyGainNetWeight = wasteType.dailyGainNetWeight,
            isOtherCategory = wasteType.nameOtherCategory.isNullOrBlank().not(),
            draftState = wasteType.draftState
        )
    }
}