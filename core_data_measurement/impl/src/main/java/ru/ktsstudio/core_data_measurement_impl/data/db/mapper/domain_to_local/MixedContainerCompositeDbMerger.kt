package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import javax.inject.Inject

/**
 * Created by Igor Park on 15/10/2020.
 */
class MixedContainerCompositeDbMerger @Inject constructor() : Mapper2<
    MixedWasteContainerComposite,
    LocalMixedWasteContainer,
    LocalMixedWasteContainer
    > {
    override fun map(
        item1: MixedWasteContainerComposite,
        item2: LocalMixedWasteContainer
    ): LocalMixedWasteContainer = with(item1) {
        return item2.copy(
            isUnique = isUnique,
            mnoContainerId = mnoContainer?.id,
            containerTypeId = containerType.id,
            containerName = uniqueName,
            containerVolume = uniqueVolume,
            containerFullness = containerFullness,
            wasteVolume = wasteVolume,
            dailyGainVolume = dailyGainVolume,
            netWeight = netWeight,
            dailyGainNetWeight = dailyGainNetWeight,
            emptyContainerWeight = emptyContainerWeight,
            filledContainerWeight = filledContainerWeight,
            draftState = draftState
        )
    }
}
