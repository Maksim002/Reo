package ru.ktsstudio.feature_map.domain

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.ktsstudio.feature_map.data.MapRepository
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.common.domain.filter.doIfFilterChange
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.rx.Rx3Completable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.errorIfEmpty
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.feature_map.data.CameraPositionStore
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.feature_map.domain.models.CameraPosition
import ru.ktsstudio.feature_map.domain.models.Center
import ru.ktsstudio.common.domain.models.MapObject
import ru.ktsstudio.common.utils.rx.alsoCompletable
import ru.ktsstudio.common.utils.rx.toRx2Single
import ru.ktsstudio.feature_map.presentation.RecycleObject
import ru.ktsstudio.utilities.extensions.zipWithTimer

/**
 * Created by Igor Park on 02/10/2020.
 */
internal class MapActor(
    private val locationRepository: LocationRepository,
    private val mapRepository: MapRepository,
    private val cameraPositionStore: CameraPositionStore,
    private val schedulers: SchedulerProvider,
    private val filterUpdater: FilterUpdater
) : Actor<MapFeature.State, MapFeature.Wish, MapFeature.Effect> {
    private val interruptSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: MapFeature.State,
        action: MapFeature.Wish
    ): Observable<out MapFeature.Effect> {
        return when (action) {
            is MapFeature.Wish.InitCameraPosition -> initCameraPosition()

            is MapFeature.Wish.ObserveLocationAvailability -> observeLocationAvailability()

            is MapFeature.Wish.UpdateRegionForPosition -> {
                if (state.isCameraPositionInitialized.not()) return Observable.empty()
                updateVisibleRegion(action.position, state.currentFilter, state.selectedObjectIds)
                    .startWith(MapFeature.Effect.UpdatingRegion(action.position))
            }
            is MapFeature.Wish.ChangeFilter -> {
                changeFilter(state, action.newFilter)
            }

            is MapFeature.Wish.ChangeSearchQuery -> {
                val updatedFilter = state.currentFilter.copy(searchQuery = action.searchQuery)
                changeFilter(state, updatedFilter)
            }

            is MapFeature.Wish.ShowMyLocation -> openMyLocation(state.cameraPosition.center.zoom)
            is MapFeature.Wish.ShowLocation -> {
                Observable.just(
                    MapFeature.Effect.CenterLocation(
                        Center(
                            gpsPoint = action.point,
                            zoom = state.cameraPosition.center.zoom
                        ),
                        showObjectInfo = true
                    )
                )
            }

            is MapFeature.Wish.CheckGpsState -> getGpsState()

            is MapFeature.Wish.SelectObjects -> {
                Observable.just(MapFeature.Effect.ObjectsSelected(action.objectIds))
            }

            is MapFeature.Wish.ClearObjects -> {
                updateVisibleRegion(
                    position = state.cameraPosition,
                    filter = state.currentFilter,
                    selectedObjectIds = emptyList()
                )
                    .startWithArray(
                        MapFeature.Effect.ObjectsSelected(emptyList()),
                        MapFeature.Effect.UpdatingRegion(state.cameraPosition)
                    )
            }

            is MapFeature.Wish.ShowSelectedObjectInfo -> {
                Observable.just(MapFeature.Effect.ShowObjectsInfo)
            }
        }
    }

    private fun observeLocationAvailability(): Observable<MapFeature.Effect> {
        return locationRepository.observeLocationPermissionState()
            .map<MapFeature.Effect>(MapFeature.Effect::ChangeLocationAvailability)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun changeFilter(
        state: MapFeature.State,
        updatedFilter: Filter
    ): Observable<MapFeature.Effect> {
        if (state.isCameraPositionInitialized.not()) return Observable.just(
            MapFeature.Effect.UpdatingFilter(updatedFilter)
        )
        return doIfFilterChange(
            updatedFilter,
            state.currentFilter,
            MapFeature.Effect.UpdatingFilter(updatedFilter)
        ) {
            filterUpdater.updateFilter(updatedFilter)
            updateVisibleRegion(state.cameraPosition, updatedFilter, state.selectedObjectIds)
        }
    }

    private fun initCameraPosition(): Observable<MapFeature.Effect> {
        return cameraPositionStore.getLastCameraPosition()
            .map<MapFeature.Effect> { cameraPosition ->
                MapFeature.Effect.CenterLocation(cameraPosition.center)
            }
            .switchIfEmpty(
                locationRepository.getCurrentLocation()
                    .map<MapFeature.Effect> { myLocation ->
                        MapFeature.Effect.CenterMyLocation(
                            Center(
                                gpsPoint = myLocation,
                                zoom = DEFAULT_ZOOM
                            )
                        )
                    }
                    .errorIfEmpty()
                    .onErrorReturnItem(
                        MapFeature.Effect.CenterLocation(
                            Center(
                                gpsPoint = DEFAULT_GPS_POINT,
                                zoom = DEFAULT_ZOOM
                            )
                        )
                    )
            )
            .observeOn(schedulers.ui)
            .toObservable()
            .toRx2Observable()
    }

    private fun updateVisibleRegion(
        position: CameraPosition,
        filter: Filter,
        selectedObjectIds: List<String>
    ): Observable<MapFeature.Effect> {
        interruptCurrentUpdate()
        return mapRepository.getRecycleObjectsForArea(
            position.topLeft,
            position.bottomRight,
            filter
        )
            .alsoCompletable { saveLastCameraPosition(position) }
            .toObservable()
            .map { it.convertToMapObjects(selectedObjectIds) }
            .map<MapFeature.Effect>(MapFeature.Effect::LoadSuccess)
            .onErrorReturn(MapFeature.Effect::LoadFailed)
            .takeUntil(interruptSignal)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun openMyLocation(zoom: Float): Observable<MapFeature.Effect> {
        return locationRepository.getCurrentLocation()
            .zipWithTimer(LOAD_DELAY)
            .errorIfEmpty()
            .toObservable()
            .map<MapFeature.Effect> { gpsPoint ->
                MapFeature.Effect.CenterMyLocation(
                    Center(
                        gpsPoint = gpsPoint,
                        zoom = zoom
                    )
                )
            }
            .onErrorReturn { MapFeature.Effect.MyLocationFailed(it) }
            .startWithItem(MapFeature.Effect.MyLocationLoading)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun getGpsState(): Observable<MapFeature.Effect> {
        return locationRepository.checkIfGpsEnabled()
            .toRx2Single()
            .toObservable()
            .map { gpsEnabled ->
                val gpsState = if (gpsEnabled) GpsState.ENABLED else GpsState.DISABLED
                MapFeature.Effect.GpsAvailabilityState(gpsState)
            }
    }

    private fun saveLastCameraPosition(position: CameraPosition): Rx3Completable {
        return cameraPositionStore.saveLastCameraPosition(position)
            .onErrorComplete()
            .subscribeOn(schedulers.io)
    }

    private fun interruptCurrentUpdate() {
        interruptSignal.onNext(Unit)
    }

    private fun List<MapObject>.convertToMapObjects(selectedObjectsIds: List<String>): List<RecycleObject> {
        return map { recycleObject ->
            RecycleObject(
                mapObject = recycleObject,
                isSelected = selectedObjectsIds.contains(recycleObject.id)
            )
        }
    }

    companion object {
        private val DEFAULT_GPS_POINT = GpsPoint(55.75222, 37.61556)
        private const val DEFAULT_ZOOM = 15f
        private const val LOAD_DELAY = 500L
    }
}
