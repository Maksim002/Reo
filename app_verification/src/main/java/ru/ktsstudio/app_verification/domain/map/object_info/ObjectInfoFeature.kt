package ru.ktsstudio.app_verification.domain.map.object_info

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.app_verification.domain.models.Route
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject

/**
 * Created by Igor Park on 05/10/2020.
 */
class ObjectInfoFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    ObjectInfoFeature.Wish,
    ObjectInfoFeature.Effect,
    ObjectInfoFeature.State,
    ObjectInfoFeature.News>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        bootstrapper = null,
        newsPublisher = newsPublisher
    ) {

    data class State(
        val infoList: List<VerificationObject>,
        val destination: GpsPoint?,
        val isLoading: Boolean,
        val error: Throwable?
    )

    sealed class Wish {
        data class LoadData(val objectIds: List<String>) : Wish()
        object OpenMapWithRoute : Wish()
        data class CheckGpsState(val destination: GpsPoint) : Wish()
    }

    sealed class Effect {
        object Loading : Effect()
        data class Success(val infoList: List<VerificationObject>) : Effect()
        data class Error(override val throwable: Throwable) : Effect(), ErrorEffect
        data class NavigateToMapWithRoute(val route: Route) : Effect()
        data class GpsAvailabilityState(val gpsState: GpsState, val destination: GpsPoint) : Effect()
        object LocationNotAvailable : Effect()
    }

    sealed class News {
        data class NavigateToMapWithRoute(val route: Route) : News()
        data class GpsAvailabilityState(val gpsState: GpsState) : News()
        object ShowLocationNotAvailableToast : News()
    }
}
