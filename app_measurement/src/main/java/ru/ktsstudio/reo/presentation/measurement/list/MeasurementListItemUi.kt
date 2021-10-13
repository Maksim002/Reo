package ru.ktsstudio.reo.presentation.measurement.list

import androidx.annotation.ColorRes
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
internal data class MeasurementListItemUi(
    val id: Long,
    val sourceName: String,
    val categoryName: String,
    val measurementAvailability: Boolean,
    val address: String,
    val measurementCreatedDate: String,
    val measurementStatus: MeasurementStatus,
    @ColorRes val statusColor: Int
)
