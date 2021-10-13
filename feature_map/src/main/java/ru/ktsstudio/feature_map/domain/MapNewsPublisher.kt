package ru.ktsstudio.feature_map.domain

import com.badoo.mvicore.element.NewsPublisher
import ru.ktsstudio.feature_map.domain.models.AutoCentring

internal class MapNewsPublisher : NewsPublisher<
    MapFeature.Wish,
    MapFeature.Effect,
    MapFeature.State,
    MapFeature.News
    > {

    override fun invoke(
        action: MapFeature.Wish,
        effect: MapFeature.Effect,
        state: MapFeature.State
    ): MapFeature.News? {
        return when (effect) {
            is MapFeature.Effect.LoadFailed -> MapFeature.News.LoadFailed(effect.throwable)
            is MapFeature.Effect.MyLocationFailed -> MapFeature.News.MyLocationFailed(effect.throwable)
            is MapFeature.Effect.CenterLocation,
            is MapFeature.Effect.CenterMyLocation -> {
                effect as AutoCentring
                MapFeature.News.ShowLocation(
                    point = effect.destination.gpsPoint,
                    zoom = effect.destination.zoom,
                    showObjectInfo = effect is MapFeature.Effect.CenterLocation && effect.showObjectInfo
                )
                    .takeIf { state.isAutoCentringAllowed }
            }
            is MapFeature.Effect.GpsAvailabilityState -> MapFeature.News.GpsAvailabilityState(
                effect.gpsState
            )
            is MapFeature.Effect.LoadSuccess -> {
                MapFeature.News.ShowRecycleObjects(effect.recycleObjects)
            }

            is MapFeature.Effect.ShowObjectsInfo -> {
                state.selectedObjectIds.takeIf { it.isNotEmpty() }?.let {
                    MapFeature.News.ShowObjectInfo(state.selectedObjectIds)
                }
            }
            else -> null
        }
    }
}
