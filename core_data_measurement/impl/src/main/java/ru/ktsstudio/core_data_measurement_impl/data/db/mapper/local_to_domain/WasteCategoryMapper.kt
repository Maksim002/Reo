package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory
import javax.inject.Inject

class WasteCategoryMapper @Inject constructor() : Mapper<LocalWasteCategory, WasteCategory> {
    override fun map(item: LocalWasteCategory): WasteCategory {
        return WasteCategory(
            id = item.id,
            name = item.name
        )
    }
}