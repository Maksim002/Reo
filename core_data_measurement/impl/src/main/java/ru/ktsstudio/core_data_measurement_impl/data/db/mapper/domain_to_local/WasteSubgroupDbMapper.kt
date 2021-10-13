package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteSubgroup
import javax.inject.Inject

/**
 * Created by Igor Park on 27/11/2020.
 */
class WasteSubgroupDbMapper @Inject constructor() : Mapper<WasteSubgroup, LocalWasteSubgroup> {
    override fun map(item: WasteSubgroup): LocalWasteSubgroup = with(item){
        return LocalWasteSubgroup(
            id = id,
            name = name,
            groupId = groupId
        )
    }
}