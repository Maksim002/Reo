package ru.ktsstudio.reo.presentation.measurement.morphology.item_info

/**
 * Created by Igor Park on 25/10/2020.
 */
data class EditMorphologyItemUiState(
    val fields: List<Any>,
    val isReady: Boolean,
    val isLoading: Boolean,
    val error: Throwable?
)
