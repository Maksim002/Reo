package ru.ktsstudio.app_verification.domain.map.object_info

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.ktsstudio.app_verification.domain.models.Route
import ru.ktsstudio.common.utils.mvi.refreshList
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.errorIfEmpty
import ru.ktsstudio.utilities.extensions.zipWithTimer

class ObjectInfoActor(
    private val objectRepository: ObjectRepository,
    private val locationRepository: LocationRepository,
    private val schedulers: SchedulerProvider
) : Actor<
    ObjectInfoFeature.State,
    ObjectInfoFeature.Wish,
    ObjectInfoFeature.Effect
    > {

    override fun invoke(
        state: ObjectInfoFeature.State,
        action: ObjectInfoFeature.Wish
    ): Observable<out ObjectInfoFeature.Effect> {

        val wishToEffect = when (action) {

            is ObjectInfoFeature.Wish.LoadData -> refreshList(
                createFetchAction = {
                    objectRepository.getObjectListByIds(action.objectIds)
                        .zipWithTimer(FETCH_DELAY, schedulers.computation)
                },
                createLoadingAction = { ObjectInfoFeature.Effect.Loading },
                createSuccessAction = (ObjectInfoFeature.Effect::Success),
                createErrorAction = { ObjectInfoFeature.Effect.Error(it) }
            )

            is ObjectInfoFeature.Wish.OpenMapWithRoute -> locationRepository.getCurrentLocation()
                .map<ObjectInfoFeature.Effect> { currentLocation ->
                    ObjectInfoFeature.Effect.NavigateToMapWithRoute(
                        Route(
                            start = currentLocation,
                            destination = state.destination ?: error("$action triggered without destination")
                        )
                    )
                }
                .errorIfEmpty()
                .onErrorReturnItem(ObjectInfoFeature.Effect.LocationNotAvailable)
                .toObservable()

            is ObjectInfoFeature.Wish.CheckGpsState -> getGpsState(action.destination)
        }

        return wishToEffect.observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun getGpsState(destination: GpsPoint): Rx3Observable<ObjectInfoFeature.Effect> {
        return locationRepository.checkIfGpsEnabled()
            .flatMapObservable { gpsEnabled ->
                val gpsState = if (gpsEnabled) GpsState.ENABLED else GpsState.DISABLED
                Rx3Observable.just(ObjectInfoFeature.Effect.GpsAvailabilityState(gpsState, destination))
            }
    }

    companion object {
        private const val FETCH_DELAY = 500L
    }
}
