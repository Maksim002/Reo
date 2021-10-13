package ru.ktsstudio.reo.domain.measurement.edit_mixed_container

import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature.Effect
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature.State

/**
 * Created by Igor Park on 10/10/2020.
 */

class EditMixedContainerReducer : Reducer<State, Effect> {
    override fun invoke(state: State, effect: Effect): State {
        return when (effect) {
            is Effect.DataLoading -> {
                state.copy(
                    isLoading = true,
                    isUpdating = false,
                    error = null
                )
            }
            is Effect.DataLoadError -> {
                state.copy(
                    isLoading = false,
                    isUpdating = false,
                    error = effect.throwable
                )
            }

            is Effect.DataInitialized -> {
                state.copy(
                    isLoading = false,
                    isUpdating = false,
                    error = null,
                    containerDraft = effect.containerDraft
                )
            }

            is Effect.DataUpdating -> {
                state.copy(
                    isLoading = false,
                    isUpdating = true,
                    error = null
                )
            }

            is Effect.DataUpdateCompleted -> {
                state.copy(
                    isLoading = false,
                    isUpdating = false,
                    error = null
                )
            }

            is Effect.DataUpdateFailed,
            is Effect.FormChanged -> state
        }
    }
}
