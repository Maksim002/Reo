package ru.ktsstudio.reo.domain.measurement.start

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect

/**
 * Created by Igor Park on 10/10/2020.
 */
class StartMeasurementFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    StartMeasurementFeature.Wish,
    StartMeasurementFeature.Effect,
    StartMeasurementFeature.State,
    StartMeasurementFeature.News
    >(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher,
        bootstrapper = null
    ) {
    data class State(
        val isUpdating: Boolean,
        val isMnoActive: Boolean,
        val isOptionSelected: Boolean,
        val comment: String
    )

    sealed class Wish {
        data class SetMnoActiveState(val isActive: Boolean) : Wish()
        data class ResolveMeasurement(val mnoId: String) : Wish()
        data class SkipMeasurement(
            val mnoId: String,
            val impossibilityReason: String,
            val withLocation: Boolean
        ) : Wish()
        data class AddComment(val comment: String) : Wish()
    }

    sealed class Effect {
        data class MnoActiveStateChange(val isActive: Boolean) : Effect()
        object MeasurementUpdating : Effect()
        object MeasurementSkipped : Effect()
        data class CommentAdded(val comment: String) : Effect()
        data class MeasurementFailed(override val throwable: Throwable) : Effect(), ErrorEffect
        object MeasurementResolvedToSkipp : Effect()
        data class MeasurementResolvedToProceed(val mnoId: String) : Effect()
    }

    sealed class News {
        object MeasurementResolvedToSkip : News()
        object MeasurementSkipped : News()
        data class MeasurementFailed(val throwable: Throwable) : News()
        data class MeasurementProceed(val mnoId: String) : News()
    }
}
