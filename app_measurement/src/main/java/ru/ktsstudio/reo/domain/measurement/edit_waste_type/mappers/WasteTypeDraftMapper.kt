package ru.ktsstudio.reo.domain.measurement.edit_waste_type.mappers

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.WasteTypeDraft
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class WasteTypeDraftMapper @Inject constructor() : Mapper<
    ContainerWasteType,
    WasteTypeDraft
    > {

    override fun map(item: ContainerWasteType): WasteTypeDraft =
        with(item) {
            WasteTypeDraft(
                selectedCategoryId = categoryId,
                selectedCategoryName = categoryName,
                categoryOptions = emptyList(),
                nameOtherCategory = nameOtherCategory,
                containerVolume = containerVolume,
                containerFullness = containerFullness,
                netWeight = netWeight,
                dailyGainNetWeight = dailyGainNetWeight,
                wasteVolume = wasteVolume,
                dailyGainVolume = dailyGainVolume,
                isOtherCategory = nameOtherCategory.isNullOrBlank().not(),
                draftState = draftState
            )
        }
}
