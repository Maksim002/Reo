package ru.ktsstudio.reo.presentation.measurement.list

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
internal data class MeasurementListUiState(
    val loading: Boolean,
    val error: Throwable?,
    val data: List<MeasurementListItemUi>,
    val searchQuery: String,
    val isFilterSet: Boolean,
)
