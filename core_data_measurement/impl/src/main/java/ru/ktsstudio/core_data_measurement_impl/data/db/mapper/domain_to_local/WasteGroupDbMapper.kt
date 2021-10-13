package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroup
import javax.inject.Inject

/**
 * Created by Igor Park on 27/11/2020.
 */
class WasteGroupDbMapper @Inject constructor() : Mapper<WasteGroup, LocalWasteGroup> {
    override fun map(item: WasteGroup): LocalWasteGroup = with(item){
        return LocalWasteGroup(
            id = id,
            name = name
        )
    }
}