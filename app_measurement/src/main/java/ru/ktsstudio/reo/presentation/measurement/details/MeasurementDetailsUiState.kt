package ru.ktsstudio.reo.presentation.measurement.details

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
data class MeasurementDetailsUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<Any>,
    val isMeasurementCreating: Boolean,
    val isEditable: Boolean
)
