package ru.ktsstudio.reo.presentation.measurement.create_measurement

/**
 * Created by Igor Park on 15/10/2020.
 */
data class CreateMeasurementUiState(
    val measurementId: Long?,
    val fields: List<Any>,
    val isReady: Boolean,
    val isLoading: Boolean,
    val error: Throwable?
)
