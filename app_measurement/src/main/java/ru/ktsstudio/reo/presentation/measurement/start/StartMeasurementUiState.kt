package ru.ktsstudio.reo.presentation.measurement.start

/**
 * Created by Igor Park on 11/10/2020.
 */
data class StartMeasurementUiState(
    val isMnoActive: Boolean,
    val isLoading: Boolean,
    val isOptionSelected: Boolean,
    val comment: String,
    val measurementActionText: String
) {
    val isReady: Boolean
        get() = isMnoActive || comment.isNotBlank()
}
