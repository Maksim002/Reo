package ru.ktsstudio.feature_map.domain

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class MapReducer : Reducer<MapFeature.State, MapFeature.Effect> {

    override fun invoke(state: MapFeature.State, effect: MapFeature.Effect): MapFeature.State {
        return when (effect) {
            is MapFeature.Effect.UpdatingRegion -> {
                state.copy(
                    isAutoCentringAllowed = false,
                    cameraPosition = effect.cameraPosition,
                )
            }
            is MapFeature.Effect.UpdatingFilter -> {
                state.copy(
                    isAutoCentringAllowed = false,
                    currentFilter = effect.filter
                )
            }

            is MapFeature.Effect.ChangeLocationAvailability -> {
                state.copy(isMyLocationEnabled = effect.isEnabled)
            }
            is MapFeature.Effect.MyLocationFailed -> {
                state.copy(isMyLocationLoading = false)
            }

            is MapFeature.Effect.CenterLocation -> {
                state.copy(
                    isCameraPositionInitialized = true,
                    isAutoCentringAllowed = true
                )
            }

            is MapFeature.Effect.CenterMyLocation -> {
                state.copy(
                    isCameraPositionInitialized = true,
                    isAutoCentringAllowed = true,
                    isMyLocationLoading = false
                )
            }

            is MapFeature.Effect.MyLocationLoading -> {
                state.copy(isMyLocationLoading = true)
            }

            is MapFeature.Effect.ObjectsSelected -> {
                state.copy(selectedObjectIds = effect.objectIds)
            }

            is MapFeature.Effect.LoadSuccess,
            is MapFeature.Effect.LoadFailed,
            is MapFeature.Effect.GpsAvailabilityState,
            is MapFeature.Effect.ShowObjectsInfo -> state
        }
    }
}