package ru.ktsstudio.reo.presentation.measurement.edit_mixed_container

/**
 * Created by Igor Park on 25/10/2020.
 */
data class EditMixedContainerUiState(
    val containerFields: List<Any>,
    val isReady: Boolean,
    val isLoading: Boolean,
    val isUpdating: Boolean,
    val error: Throwable?
)
