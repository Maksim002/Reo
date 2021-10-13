package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.data.model.MixedWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.MeasurementLocalId
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainer
import javax.inject.Inject

class MixedWasteContainerDbMapper @Inject constructor() :
    Mapper2<MixedWasteContainer, MeasurementLocalId, LocalMixedWasteContainer> {

    override fun map(
        item1: MixedWasteContainer,
        item2: MeasurementLocalId
    ): LocalMixedWasteContainer {
        return LocalMixedWasteContainer(
            measurementLocalId = item2,
            isUnique = item1.isUnique,
            mnoContainerId = item1.mnoContainerId.takeIf { item1.isUnique.not() },
            mnoUniqueContainerId = item1.mnoContainerId.takeIf { item1.isUnique },
            containerTypeId = item1.containerType.id,
            containerName = item1.containerName,
            containerVolume = item1.containerVolume,
            containerFullness = item1.containerFullness,
            wasteVolume = item1.wasteVolume,
            dailyGainVolume = item1.dailyGainVolume,
            netWeight = item1.netWeight,
            dailyGainNetWeight = item1.dailyGainNetWeight,
            emptyContainerWeight = item1.emptyContainerWeight,
            filledContainerWeight = item1.filledContainerWeight,
            draftState = DraftState.IDLE
        )
    }
}