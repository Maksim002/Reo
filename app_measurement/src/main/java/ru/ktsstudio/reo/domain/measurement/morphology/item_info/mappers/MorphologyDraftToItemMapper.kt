package ru.ktsstudio.reo.domain.measurement.morphology.item_info.mappers

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.MorphologyItemDraft
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class MorphologyDraftToItemMapper @Inject constructor() : Mapper2<
    MorphologyItemDraft,
    Long,
    MorphologyItem
    > {

    override fun map(item1: MorphologyItemDraft, item2: Long): MorphologyItem = with(item1) {
        return MorphologyItem(
            localId = item2,
            group = selectedWasteGroup.requireNotNull(),
            subgroup = selectedWasteSubgroup,
            dailyGainWeight = dailyGainWeight.requireNotNull(),
            dailyGainVolume = dailyGainVolume.requireNotNull(),
            draftState = draftState
        )
    }
}
