package ru.ktsstudio.reo.domain.measurement.start

import com.badoo.mvicore.element.NewsPublisher
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature.Effect
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature.News
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature.State
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature.Wish

/**
 * Created by Igor Park on 10/10/2020.
 */
class StartMeasureNewsPublisher : NewsPublisher<Wish, Effect, State, News> {
    override fun invoke(action: Wish, effect: Effect, state: State): News? {
        return when (effect) {
            is Effect.MeasurementResolvedToSkipp -> News.MeasurementResolvedToSkip
            is Effect.MeasurementSkipped -> News.MeasurementSkipped
            is Effect.MeasurementFailed -> News.MeasurementFailed(effect.throwable)
            is Effect.MeasurementResolvedToProceed -> News.MeasurementProceed(effect.mnoId)
            is Effect.MeasurementUpdating,
            is Effect.CommentAdded,
            is Effect.MnoActiveStateChange -> null
        }
    }
}
