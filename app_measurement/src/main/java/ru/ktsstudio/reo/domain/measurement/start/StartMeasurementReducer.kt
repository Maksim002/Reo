package ru.ktsstudio.reo.domain.measurement.start

import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature.Effect
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature.State

/**
 * Created by Igor Park on 10/10/2020.
 */
class StartMeasurementReducer : Reducer<State, Effect> {
    override fun invoke(state: State, effect: Effect): State {
        return when (effect) {
            is Effect.MnoActiveStateChange -> state.copy(
                isMnoActive = effect.isActive,
                isOptionSelected = true
            )
            is Effect.MeasurementUpdating -> state.copy(
                isUpdating = true
            )
            is Effect.MeasurementFailed -> state.copy(
                isUpdating = false
            )
            is Effect.MeasurementSkipped -> state.copy(
                isUpdating = false
            )
            is Effect.CommentAdded -> state.copy(
                comment = effect.comment
            )
            is Effect.MeasurementResolvedToSkipp,
            is Effect.MeasurementResolvedToProceed -> state
        }
    }
}
