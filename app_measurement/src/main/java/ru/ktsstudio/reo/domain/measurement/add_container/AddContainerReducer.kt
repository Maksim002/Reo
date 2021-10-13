package ru.ktsstudio.reo.domain.measurement.add_container

import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerFeature.Effect
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerFeature.State

/**
 * Created by Igor Park on 10/10/2020.
 */
class AddContainerReducer : Reducer<State, Effect> {
    override fun invoke(state: State, effect: Effect): State {
        return when (effect) {
            is Effect.DataLoading -> {
                state.copy(
                    isLoading = true,
                    error = null
                )
            }
            is Effect.DataLoadingError -> {
                state.copy(
                    isLoading = false,
                    error = effect.throwable
                )
            }

            is Effect.DataInitialized -> {
                state.copy(
                    isLoading = false,
                    error = null,
                    isNewContainer = false,
                    containerTypes = effect.containerTypes,
                    mnoContainers = effect.mnoContainers
                )
            }
            is Effect.OptionSelected -> state.copy(
                isNewContainer = effect.isNewContainer,
                selectedMnoContainerId = effect.selectedMnoContainerId,
                selectedContainerTypeId = effect.selectedContainerTypeId
            )
        }
    }
}
