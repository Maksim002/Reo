package ru.ktsstudio.reo.domain.measurement.details

import com.badoo.mvicore.element.NewsPublisher
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.Effect
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.News
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.State
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature.Wish

internal class MeasurementDetailsNewsPublisher : NewsPublisher<Wish, Effect, State, News> {
    override fun invoke(action: Wish, effect: Effect, state: State): News? {
        return when (effect) {
            is Effect.MeasurementCreateFailed -> News.MeasurementCreateFailed(effect.throwable)
            is Effect.MeasurementCreated -> News.MeasurementCreated
            is Effect.OpenEditMeasurement -> News.OpenEditMeasurement(effect.mnoId, effect.measurementId)
            else -> null
        }
    }
}
