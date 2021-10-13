package ru.ktsstudio.reo.presentation.measurement.morphology.section

/**
 * Created by Igor Park on 25/10/2020.
 */
data class EditMorphologyUiState(
    val morphologyList: List<Any>,
    val isLoading: Boolean,
    val error: Throwable?
)
