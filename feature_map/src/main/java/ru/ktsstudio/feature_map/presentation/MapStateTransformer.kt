package ru.ktsstudio.feature_map.presentation

import ru.ktsstudio.feature_map.domain.MapFeature
import ru.ktsstudio.feature_map.ui.MapUiState
import timber.log.Timber

internal class MapStateTransformer : (MapFeature.State) -> MapUiState {

    override fun invoke(mapFeatureState: MapFeature.State): MapUiState {
        return MapUiState(
            isMyLocationLoading = mapFeatureState.isMyLocationLoading,
            isMyLocationEnabled = mapFeatureState.isMyLocationEnabled,
            searchQuery = mapFeatureState.currentFilter.searchQuery,
            isFilterSet = mapFeatureState.currentFilter.filterMap.isNotEmpty()
        )
    }
}