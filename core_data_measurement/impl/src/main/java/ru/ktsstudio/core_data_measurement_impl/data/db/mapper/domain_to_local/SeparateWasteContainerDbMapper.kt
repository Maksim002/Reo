package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.data.model.SeparateWasteContainer
import ru.ktsstudio.core_data_measurement_impl.data.MeasurementLocalId
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainer
import javax.inject.Inject

class SeparateWasteContainerDbMapper @Inject constructor() :
    Mapper2<SeparateWasteContainer, MeasurementLocalId, LocalSeparateWasteContainer> {

    override fun map(
        item1: SeparateWasteContainer,
        item2: MeasurementLocalId
    ): LocalSeparateWasteContainer {
        return LocalSeparateWasteContainer(
            measurementLocalId = item2,
            mnoContainerId = item1.mnoContainerId?.takeIf { item1.isUnique.not() },
            mnoUniqueContainerId = item1.mnoContainerId?.takeIf { item1.isUnique },
            isUnique = item1.isUnique,
            containerName = item1.containerName,
            containerVolume = item1.containerVolume,
            containerTypeId = item1.containerType.id,
            draftState = DraftState.IDLE
        )
    }
}