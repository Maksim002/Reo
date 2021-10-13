package ru.ktsstudio.reo.ui.measurement.create.adapters

import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.reo.presentation.measurement.create_measurement.MeasurementMediaUi

/**
 * Created by Igor Park on 16/11/2020.
 */
data class MeasurementMediaListItem(
    val title: String,
    val mediaList: List<MeasurementMediaUi>,
    val category: MeasurementMediaCategory
)
