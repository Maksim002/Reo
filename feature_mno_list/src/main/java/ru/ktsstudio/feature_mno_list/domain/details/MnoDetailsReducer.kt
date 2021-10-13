package ru.ktsstudio.feature_mno_list.domain.details

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 03.10.2020.
 */
internal class MnoDetailsReducer : Reducer<MnoDetailsFeature.State, MnoDetailsFeature.Effect> {
    override fun invoke(
        state: MnoDetailsFeature.State,
        effect: MnoDetailsFeature.Effect
    ): MnoDetailsFeature.State {
        return when (effect) {
            MnoDetailsFeature.Effect.Loading -> state.copy(
                loading = true,
                error = null
            )
            is MnoDetailsFeature.Effect.Error -> {
                state.copy(
                    loading = false,
                    error = effect.throwable
                )
            }
            is MnoDetailsFeature.Effect.Success -> {
                state.copy(
                    loading = false,
                    error = null,
                    mno = effect.mnoDetails,
                    measurements = effect.mnoMeasurements
                )
            }
        }
    }
}