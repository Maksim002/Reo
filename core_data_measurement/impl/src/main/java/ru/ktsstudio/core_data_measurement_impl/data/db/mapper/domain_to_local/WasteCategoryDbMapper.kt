package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory
import javax.inject.Inject

class WasteCategoryDbMapper @Inject constructor() : Mapper<WasteCategory, LocalWasteCategory> {
    override fun map(item: WasteCategory): LocalWasteCategory {
        return LocalWasteCategory(
            id = item.id,
            name = item.name
        )
    }
}