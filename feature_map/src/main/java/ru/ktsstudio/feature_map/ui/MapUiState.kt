package ru.ktsstudio.feature_map.ui

/**
 * Created by Igor Park on 05/10/2020.
 */
internal data class MapUiState(
    val isMyLocationLoading: Boolean,
    val isMyLocationEnabled: Boolean,
    val isFilterSet: Boolean,
    val searchQuery: String
)