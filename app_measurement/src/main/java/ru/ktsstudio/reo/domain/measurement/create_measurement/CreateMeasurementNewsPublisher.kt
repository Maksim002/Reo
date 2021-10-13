package ru.ktsstudio.reo.domain.measurement.create_measurement

import com.badoo.mvicore.element.NewsPublisher
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature.Effect
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature.News
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature.State
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature.Wish

/**
 * Created by Igor Park on 10/10/2020.
 */
class CreateMeasurementNewsPublisher : NewsPublisher<Wish, Effect, State, News> {
    override fun invoke(action: Wish, effect: Effect, state: State): News? {
        return when (effect) {
            is Effect.AddMediaFailed -> News.AddMediaFailed(effect.throwable)
            is Effect.CapturingMediaReady -> News.CaptureMedia(effect.capturingMedia)
            is Effect.DeleteMediaFailed -> News.DeleteMediaFailed(effect.throwable)
            is Effect.ExitMeasurement -> News.ExitMeasurement
            is Effect.ConfirmDataClear -> News.ConfirmDataClear
            is Effect.GpsAvailabilityState -> News.GpsAvailabilityState(effect.state)
            is Effect.SetLocationCompleted -> News.SetLocationCompleted
            is Effect.SetLocationFailed -> News.SetLocationFailed(effect.throwable)
            else -> null
        }
    }
}
