package ru.ktsstudio.reo.domain.measurement.create_measurement

import android.net.Uri
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import java.io.File

/**
 * Created by Igor Park on 17/10/2020.
 */
data class CapturingMedia(
    val outputUri: Uri,
    val cachedFile: File,
    val measurementMediaCategory: MeasurementMediaCategory,
    val withLocation: Boolean
)
