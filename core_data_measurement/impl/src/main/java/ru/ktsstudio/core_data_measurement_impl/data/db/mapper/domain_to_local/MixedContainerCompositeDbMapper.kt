package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import javax.inject.Inject

/**
 * Created by Igor Park on 15/10/2020.
 */
class MixedContainerCompositeDbMapper @Inject constructor() : Mapper2<
    MixedWasteContainerComposite,
    Long,
    LocalMixedWasteContainer
    > {
    override fun map(item1: MixedWasteContainerComposite, item2: Long): LocalMixedWasteContainer =
        with(item1) {
            return LocalMixedWasteContainer(
                localId = localId,
                measurementLocalId = item2,
                isUnique = isUnique,
                mnoContainerId = mnoContainer?.id.takeIf { isUnique.not() },
                mnoUniqueContainerId = mnoContainer?.id.takeIf { isUnique },
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