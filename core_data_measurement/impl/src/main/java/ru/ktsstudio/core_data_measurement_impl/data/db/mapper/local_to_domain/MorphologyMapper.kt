package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphologyWithRelation
import javax.inject.Inject

/**
 * Created by Igor Park on 31/10/2020.
 */
class MorphologyMapper @Inject constructor() :
    Mapper<LocalMorphologyWithRelation, MorphologyItem> {
    override fun map(item: LocalMorphologyWithRelation): MorphologyItem = with(item) {
        return MorphologyItem(
            localId = morphology.localId,
            group = WasteGroup(
                id = wasteGroup.id,
                name = wasteGroup.name
            ),
            subgroup = wasteSubgroup?.let {
                WasteSubgroup(
                    id = wasteSubgroup.id,
                    name = wasteSubgroup.name,
                    groupId = wasteSubgroup.groupId
                )
            },
            dailyGainWeight = morphology.dailyGainWeight,
            dailyGainVolume = morphology.dailyGainVolume,
            draftState = morphology.draftState
        )
    }
}