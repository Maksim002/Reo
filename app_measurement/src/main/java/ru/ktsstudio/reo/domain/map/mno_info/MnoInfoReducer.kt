package ru.ktsstudio.reo.domain.map.mno_info

import com.badoo.mvicore.element.Reducer

class MnoInfoReducer : Reducer<MnoInfoFeature.State, MnoInfoFeature.Effect> {
    override fun invoke(
        state: MnoInfoFeature.State,
        effect: MnoInfoFeature.Effect
    ): MnoInfoFeature.State {
        return when (effect) {
            MnoInfoFeature.Effect.Loading -> state.copy(
                error = null,
                isLoading = true
            )
            is MnoInfoFeature.Effect.Error -> state.copy(
                isLoading = false,
                error = effect.throwable
            )
            is MnoInfoFeature.Effect.Success -> state.copy(
                isLoading = false,
                error = null,
                infoList = effect.infoList
            )
        }
    }
}
