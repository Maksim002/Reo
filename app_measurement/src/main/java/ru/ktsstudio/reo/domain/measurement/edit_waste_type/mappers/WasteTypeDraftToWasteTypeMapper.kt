package ru.ktsstudio.reo.domain.measurement.edit_waste_type.mappers

import ru.ktsstudio.common.utils.checkValue
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.WasteTypeDraft
import javax.inject.Inject

/**
 * Created by Igor Park on 27/10/2020.
 */
class WasteTypeDraftToWasteTypeMapper @Inject constructor() : Mapper2<
    WasteTypeDraft,
    String,
    ContainerWasteType
    > {

    override fun map(item1: WasteTypeDraft, item2: String): ContainerWasteType = with(item1) {
        return ContainerWasteType(
            localId = item2,
            nameOtherCategory = nameOtherCategory,
            categoryId = checkValue(selectedCategoryId, "categoryId"),
            categoryName = checkValue(selectedCategoryName, "categoryName"),
            containerFullness = containerFullness,
            containerVolume = containerVolume,
            wasteVolume = checkValue(wasteVolume, "wasteVolume"),
            dailyGainVolume = checkValue(dailyGainVolume, "dailyGainVolume"),
            netWeight = checkValue(netWeight, "netWeight"),
            dailyGainNetWeight = checkValue(dailyGainNetWeight, "dailyGainNetWeight"),
            isOtherCategory = item1.isOtherCategory,
            draftState = item1.draftState
        )
    }
}
