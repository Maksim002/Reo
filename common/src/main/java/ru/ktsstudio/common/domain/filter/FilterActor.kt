package ru.ktsstudio.common.domain.filter

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class FilterActor(
    private val filterUpdater: FilterUpdater
) : Actor<FilterFeature.State, FilterFeature.Wish, FilterFeature.Effect> {
    override fun invoke(
        state: FilterFeature.State,
        action: FilterFeature.Wish
    ): Observable<out FilterFeature.Effect> {
        return when (action) {
            is FilterFeature.Wish.ChangeFilterExternal -> {
                Observable.just(FilterFeature.Effect.UpdateFilter(action.newFilter))
            }
            is FilterFeature.Wish.ChangeField -> {
                val newFilter = state.currentFilter.copy(
                    filterMap = updateFilterKey(
                        state.currentFilter.filterMap,
                        action.fieldKey,
                        action.fieldValue
                    )
                )
                Observable.just(FilterFeature.Effect.UpdateFilter(newFilter))
            }
            is FilterFeature.Wish.Apply -> {
                filterUpdater.updateFilter(state.currentFilter)
                Observable.just(FilterFeature.Effect.FilterApplied)
            }

            is FilterFeature.Wish.Clear -> {
                Observable.just(
                    FilterFeature.Effect.UpdateFilter(
                        state.currentFilter.copy(filterMap = emptyMap())
                    )
                )
            }
        }
    }

    private fun updateFilterKey(
        filterMap: Map<FilterKey, Any>,
        key: FilterKey,
        value: Any?
    ): Map<FilterKey, Any> {
        return value?.let {
            filterMap + mapOf(key to value)
        } ?: run {
            filterMap - key
        }
    }
}