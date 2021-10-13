package ru.ktsstudio.reo.domain.measurement.morphology.item_info

import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature.Effect
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature.State

/**
 * Created by Igor Park on 10/10/2020.
 */

class EditMorphologyItemReducer : Reducer<State, Effect> {
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
                morphologyItemDraft = effect.morphologyItemDraft
            )

            is Effect.DataUpdateCompleted -> state.copy(
                isLoading = false,
                error = null
            )

            is Effect.DataUpdateFailed -> state.copy(
                isLoading = false
            )
        }
    }
}
