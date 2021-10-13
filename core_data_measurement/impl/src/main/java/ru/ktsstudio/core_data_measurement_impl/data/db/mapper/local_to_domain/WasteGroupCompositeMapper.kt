package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_api.domain.WasteGroupComposite
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroupWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteSubgroup
import javax.inject.Inject

/**
 * Created by Igor Park on 31/10/2020.
 */
class WasteGroupCompositeMapper @Inject constructor(
    private val wasteSubgroupMapper: Mapper<LocalWasteSubgroup, WasteSubgroup>
) : Mapper<LocalWasteGroupWithRelations, WasteGroupComposite> {
    override fun map(item: LocalWasteGroupWithRelations): WasteGroupComposite = with(item) {
        return WasteGroupComposite(
            group = WasteGroup(
                id = wasteGroup.id,
                name = wasteGroup.name
            ),
            subgroups = wasteSubgroups.map(wasteSubgroupMapper::map)
        )
    }
}
