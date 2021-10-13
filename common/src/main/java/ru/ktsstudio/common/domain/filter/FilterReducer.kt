package ru.ktsstudio.common.domain.filter

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class FilterReducer: Reducer<FilterFeature.State, FilterFeature.Effect> {
    override fun invoke(
        state: FilterFeature.State,
        effect: FilterFeature.Effect
    ): FilterFeature.State {
        return when(effect) {
            is FilterFeature.Effect.UpdateFilter -> state.copy(
                currentFilter = effect.filter
            )
            is FilterFeature.Effect.FilterApplied -> state
        }
    }
}