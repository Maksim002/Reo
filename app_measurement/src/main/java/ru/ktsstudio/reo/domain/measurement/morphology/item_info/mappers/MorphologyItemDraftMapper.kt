package ru.ktsstudio.reo.domain.measurement.morphology.item_info.mappers

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.MorphologyItemDraft
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class MorphologyItemDraftMapper @Inject constructor() : Mapper<
    MorphologyItem,
    MorphologyItemDraft
    > {

    override fun map(item: MorphologyItem): MorphologyItemDraft =
        with(item) {
            MorphologyItemDraft(
                selectedWasteGroup = group,
                selectedWasteSubgroup = subgroup,
                wasteGroups = emptyList(),
                wasteGroupIdToSubgroupsMap = emptyMap(),
                dailyGainVolume = dailyGainVolume,
                dailyGainWeight = dailyGainWeight,
                draftState = draftState
            )
        }
}
