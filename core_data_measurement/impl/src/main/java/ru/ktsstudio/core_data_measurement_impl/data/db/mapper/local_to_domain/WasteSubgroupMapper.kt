package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteSubgroup
import javax.inject.Inject

/**
 * Created by Igor Park on 31/10/2020.
 */
class WasteSubgroupMapper @Inject constructor() : Mapper<LocalWasteSubgroup, WasteSubgroup> {
    override fun map(item: LocalWasteSubgroup): WasteSubgroup = with(item) {
        return WasteSubgroup(id, groupId, name)
    }
}