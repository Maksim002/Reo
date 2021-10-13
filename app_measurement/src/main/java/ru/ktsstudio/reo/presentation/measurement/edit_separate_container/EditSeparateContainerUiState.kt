package ru.ktsstudio.reo.presentation.measurement.edit_separate_container

/**
 * Created by Igor Park on 25/10/2020.
 */
data class EditSeparateContainerUiState(
    val containerId: Long,
    val containerFields: List<Any>,
    val isReady: Boolean,
    val isLoading: Boolean,
    val error: Throwable?
)
