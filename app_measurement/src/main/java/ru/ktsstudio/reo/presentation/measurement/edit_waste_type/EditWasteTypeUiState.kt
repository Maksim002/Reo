package ru.ktsstudio.reo.presentation.measurement.edit_waste_type

/**
 * Created by Igor Park on 25/10/2020.
 */
data class EditWasteTypeUiState(
    val containerFields: List<Any>,
    val isReady: Boolean,
    val isLoading: Boolean,
    val error: Throwable?
)
