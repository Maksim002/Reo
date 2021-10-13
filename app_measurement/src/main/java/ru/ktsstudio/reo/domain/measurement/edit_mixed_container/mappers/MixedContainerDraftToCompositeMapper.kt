package ru.ktsstudio.reo.domain.measurement.edit_mixed_container.mappers

import ru.ktsstudio.common.utils.checkValue
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.MixedWasteContainerDraft
import javax.inject.Inject

/**
 * Created by Igor Park on 27/10/2020.
 */
class MixedContainerDraftToCompositeMapper @Inject constructor() : Mapper2<
    MixedWasteContainerDraft,
    Long,
    MixedWasteContainerComposite
    > {
    override fun map(item1: MixedWasteContainerDraft, item2: Long): MixedWasteContainerComposite =
        with(item1) {
            return MixedWasteContainerComposite(
                localId = item2,
                isUnique = isUnique,
                mnoContainer = mnoContainer,
                uniqueName = uniqueName,
                uniqueVolume = uniqueVolume,
                containerFullness = containerFullness,
                draftState = draftState,
                containerType = checkValue(containerType, "containerType"),
                wasteVolume = checkValue(wasteVolume, "wasteVolume"),
                dailyGainVolume = checkValue(dailyGainVolume, "dailyGainVolume"),
                netWeight = checkValue(netWeight, "netWeight"),
                dailyGainNetWeight = checkValue(dailyGainNetWeight, "dailyGainNetWeight"),
                emptyContainerWeight = emptyContainerWeight,
                filledContainerWeight = filledContainerWeight
            )
        }
}
