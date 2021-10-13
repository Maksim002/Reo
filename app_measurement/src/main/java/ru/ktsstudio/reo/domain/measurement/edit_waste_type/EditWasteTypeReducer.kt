package ru.ktsstudio.reo.domain.measurement.edit_waste_type

import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature.Effect
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature.State

/**
 * Created by Igor Park on 10/10/2020.
 */

class EditWasteTypeReducer : Reducer<State, Effect> {
    override fun invoke(state: State, effect: Effect): State {
        return when (effect) {
            is Effect.DataLoading -> state.copy(
                isLoading = true,
                error = null
            )

            is Effect.DataLoadError -> state.copy(
                isLoading = false,
                error = effect.throwable
            )

            is Effect.DataInitialized -> state.copy(
                isLoading = false,
                error = null,
                wasteTypeDraft = effect.wasteTypeDraft
            )

            is Effect.DataUpdateCompleted -> state.copy(
                isLoading = false,
                error = null
            )

            is Effect.DataUpdateFailed -> state.copy(
                isLoading = false
            )
            is Effect.FormChanged -> state
        }
    }
}
