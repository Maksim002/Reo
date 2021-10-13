package ru.ktsstudio.reo.domain.measurement.create_measurement

import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature.Effect
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature.State

/**
 * Created by Igor Park on 10/10/2020.
 */
class CreateMeasureReducer : Reducer<State, Effect> {
    override fun invoke(state: State, effect: Effect): State {
        return when (effect) {
            is Effect.MeasurementChanged -> {
                val commentDraft = state.measurement
                    ?.comment
                    ?: effect.measurement.comment
                state.copy(
                    isLoading = false,
                    error = null,
                    measurement = effect.measurement
                        .copy(comment = commentDraft)
                )
            }
            is Effect.CommentChanged -> state.copy(
                measurement = state.measurement?.copy(
                    comment = effect.comment
                )
            )
            is Effect.MediaProcessingStateChange -> {
                val updatedMap = state.fileProcessingState
                    .toMutableMap()
                    .apply {
                        put(effect.mediaId, effect.processingState)
                    }
                state.copy(fileProcessingState = updatedMap)
            }

            is Effect.CapturingMediaReady -> state.copy(capturingMedia = effect.capturingMedia)

            is Effect.Loading -> state.copy(
                isLoading = true,
                error = null
            )
            is Effect.LoadingFailed -> state.copy(
                isLoading = false,
                error = effect.throwable
            )

            is Effect.EditModeCheck -> state.copy(isEditMode = effect.isEditMode)
            is Effect.GpsEnableRejected -> state.copy(gpsEnableRejected = true)

            is Effect.SetLocationCompleted,
            is Effect.SetLocationFailed -> state.copy(
                isLoading = false,
                error = null
            )

            is Effect.GpsAvailabilityState,
            is Effect.ExitMeasurement,
            is Effect.ConfirmDataClear,
            is Effect.DeleteMediaFailed,
            is Effect.AddMediaFailed -> state
        }
    }
}
