package ru.ktsstudio.feature_map.domain

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.feature_map.domain.models.AutoCentring
import ru.ktsstudio.feature_map.domain.models.CameraPosition
import ru.ktsstudio.feature_map.domain.models.Center
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.feature_map.presentation.RecycleObject

/**
 * Created by Igor Park on 02/10/2020.
 */
internal class MapFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    MapFeature.Wish,
    MapFeature.Effect,
    MapFeature.State,
    MapFeature.News
    >(
    initialState = initialState,
    actor = actor,
    reducer = reducer,
    newsPublisher = newsPublisher,
    bootstrapper = null
) {
    data class State(
        val selectedObjectIds: List<String>,
        val isCameraPositionInitialized: Boolean,
        val isAutoCentringAllowed: Boolean,
        val isMyLocationLoading: Boolean,
        val isMyLocationEnabled: Boolean,
        val cameraPosition: CameraPosition,
        val currentFilter: Filter
    )

    sealed class Wish {
        object InitCameraPosition : Wish()
        data class ShowLocation(val point: GpsPoint) : Wish()
        object ShowSelectedObjectInfo : Wish()
        object ShowMyLocation : Wish()
        object CheckGpsState : Wish()
        data class SelectObjects(val objectIds: List<String>) : Wish()
        object ClearObjects : Wish()
        data class UpdateRegionForPosition(val position: CameraPosition) : Wish()
        object ObserveLocationAvailability : Wish()
        data class ChangeFilter(val newFilter: Filter) : Wish()
        data class ChangeSearchQuery(val searchQuery: String) : Wish()
    }

    sealed class Effect {
        object MyLocationLoading : Effect()
        data class MyLocationFailed(override val throwable: Throwable) : Effect(), ErrorEffect
        data class UpdatingRegion(
            val cameraPosition: CameraPosition,
        ) : Effect()

        data class GpsAvailabilityState(val gpsState: GpsState) : Effect()

        data class UpdatingFilter(
            val filter: Filter
        ) : Effect()

        data class LoadSuccess(val recycleObjects: List<RecycleObject>) : Effect()
        data class LoadFailed(override val throwable: Throwable) : Effect(), ErrorEffect

        data class CenterLocation(
            override val destination: Center,
            val showObjectInfo: Boolean = false
        ) : Effect(), AutoCentring

        data class CenterMyLocation(
            override val destination: Center
        ) : Effect(), AutoCentring

        data class ObjectsSelected(val objectIds: List<String>) : Effect()
        object ShowObjectsInfo : Effect()
        data class ChangeLocationAvailability(val isEnabled: Boolean) : Effect()
    }

    sealed class News {
        data class MyLocationFailed(val throwable: Throwable) : News()
        data class LoadFailed(val throwable: Throwable) : News()
        data class ShowLocation(
            val point: GpsPoint,
            val zoom: Float,
            val showObjectInfo: Boolean = false
        ) : News()
        data class GpsAvailabilityState(val gpsState: GpsState) : News()
        data class ShowRecycleObjects(val objects: List<RecycleObject>) : News()
        data class ShowObjectInfo(val objectIds: List<String>) : News()
    }
}
