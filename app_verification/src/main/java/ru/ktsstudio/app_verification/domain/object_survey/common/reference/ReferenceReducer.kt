package ru.ktsstudio.app_verification.domain.object_survey.common.reference

import com.badoo.mvicore.element.Reducer

internal class ReferenceReducer :
    Reducer<ReferenceFeature.State, ReferenceFeature.Effect> {
    override fun invoke(
        state: ReferenceFeature.State,
        effect: ReferenceFeature.Effect
    ): ReferenceFeature.State {
        return when (effect) {
            is ReferenceFeature.Effect.Error -> {
                state.copy(
                    error = effect.throwable,
                    loading = false
                )
            }
            is ReferenceFeature.Effect.Success -> {
                state.copy(
                    error = null,
                    references = effect.references,
                    loading = false
                )
            }
            is ReferenceFeature.Effect.Loading -> {
                state.copy(loading = true)
            }
        }
    }
}
