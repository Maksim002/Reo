package ru.ktsstudio.reo.domain.measurement.morphology.section

import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyFeature.Effect
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyFeature.State

/**
 * Created by Igor Park on 10/10/2020.
 */

class EditMorphologyReducer : Reducer<State, Effect> {
    override fun invoke(state: State, effect: Effect): State {
        return when (effect) {
            is Effect.DataChanged -> state.copy(
                morphologyList = effect.newMorphologyList,
                isLoading = false,
                error = null
            )

            is Effect.DataLoading -> {
                state.copy(
                    isLoading = true,
                    error = null
                )
            }
            is Effect.DataLoadError -> state.copy(
                isLoading = false,
                error = effect.throwable
            )
        }
    }
}
