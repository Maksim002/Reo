package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_impl.data.MeasurementLocalId
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphology
import javax.inject.Inject

class MorphologyDbMapper @Inject constructor() :
    Mapper2<MorphologyItem, MeasurementLocalId, LocalMorphology> {

    override fun map(
        item1: MorphologyItem,
        item2: MeasurementLocalId
    ): LocalMorphology = with(item1) {
        LocalMorphology(
            localId = localId,
            measurementId = item2,
            groupId = group.id,
            subgroupId = subgroup?.id,
            dailyGainVolume = dailyGainVolume,
            dailyGainWeight = dailyGainWeight,
            draftState = draftState
        )
    }
}