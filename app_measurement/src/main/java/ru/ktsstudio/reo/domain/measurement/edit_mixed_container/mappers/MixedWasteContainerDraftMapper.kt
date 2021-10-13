package ru.ktsstudio.reo.domain.measurement.edit_mixed_container.mappers

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.MixedWasteContainerDraft
import javax.inject.Inject

/**
 * Created by Igor Park on 25/10/2020.
 */
class MixedWasteContainerDraftMapper @Inject constructor() : Mapper<
    MixedWasteContainerComposite,
    MixedWasteContainerDraft
    > {
    override fun map(item: MixedWasteContainerComposite): MixedWasteContainerDraft = with(item) {
        MixedWasteContainerDraft(
            isUnique = isUnique,
            containerType = mnoContainer?.type ?: containerType,
            mnoContainer = mnoContainer,
            uniqueName = uniqueName,
            uniqueVolume = uniqueVolume,
            containerFullness = containerFullness,
            emptyContainerWeight = emptyContainerWeight,
            filledContainerWeight = filledContainerWeight,
            netWeight = netWeight,
            dailyGainNetWeight = dailyGainNetWeight,
            wasteVolume = wasteVolume,
            dailyGainVolume = dailyGainVolume,
            draftState = draftState
        )
    }
}
