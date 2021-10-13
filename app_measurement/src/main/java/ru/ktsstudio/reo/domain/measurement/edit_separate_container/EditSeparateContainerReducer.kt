package ru.ktsstudio.reo.domain.measurement.edit_separate_container

import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature.Effect
import ru.ktsstudio.reo.domain.measurement.edit_separate_container.EditSeparateContainerFeature.State

/**
 * Created by Igor Park on 10/10/2020.
 */

class EditSeparateContainerReducer : Reducer<State, Effect> {
    override fun invoke(state: State, effect: Effect): State {
        return when (effect) {
            is Effect.DataLoading -> {
                state.copy(
                    isLoading = true,
                    error = null
                )
            }
            is Effect.DataLoadError -> {
                state.copy(
                    isLoading = false,
                    error = effect.throwable
                )
            }

            is Effect.DataInitialized -> {
                state.copy(
                    isLoading = false,
                    error = null,
                    separateContainer = effect.container
                )
            }

            is Effect.WasteTypesUpdated -> {
                state.copy(
                    separateContainer = state.separateContainer.copy(
                        wasteTypes = effect.wasteTypes
                    )
                )
            }

            is Effect.DataUpdated -> {
                state.copy(
                    isLoading = false,
                    error = null
                )
            }

            is Effect.DataCleared -> state.copy(
                isLoading = false,
                error = null
            )

            is Effect.DataUpdateFailed -> state.copy(
                isLoading = false,
                error = null
            )
        }
    }
}
