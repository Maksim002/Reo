package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.data.models.LocalModelState
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurement
import javax.inject.Inject

class MeasurementDbMapper @Inject constructor() : Mapper<Measurement, LocalMeasurement> {

    override fun map(item: Measurement): LocalMeasurement = with(item) {
        LocalMeasurement(
            remoteId = measurementRemoteId,
            mnoId = mnoId,
            measurementStatusId = status.id,
            gpsPoint = gpsPoint,
            season = season,
            date = date,
            isPossible = isPossible,
            impossibilityReason = impossibilityReason,
            comment = comment,
            revisionComment = revisionComment,
            state = LocalModelState.SUCCESS
        )
    }
}