package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementStatus
import javax.inject.Inject

class MeasurementStatusMapper @Inject constructor() : Mapper<MeasurementStatus, LocalMeasurementStatus> {

    override fun map(item: MeasurementStatus): LocalMeasurementStatus {
        return LocalMeasurementStatus(id = item.id, name = item.name, order = item.order)
    }
}