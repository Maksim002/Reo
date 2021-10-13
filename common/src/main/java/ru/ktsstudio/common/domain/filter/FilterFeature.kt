package ru.ktsstudio.common.domain.filter

import ru.ktsstudio.common.utils.mvi.BaseMviFeature

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class FilterFeature(
    initialState: State,
    actor: FilterActor,
    reducer: FilterReducer,
    newsPublisher: FilterNewsPublisher,
    filterBootstrapper: FilterBootstrapper
) : BaseMviFeature<
    FilterFeature.Wish,
    FilterFeature.Effect,
    FilterFeature.State,
    FilterFeature.News>(
    initialState = initialState,
    actor = actor,
    reducer = reducer,
    bootstrapper = filterBootstrapper,
    newsPublisher = newsPublisher,
) {

    data class State(
        val currentFilter: Filter = Filter.EMPTY
    )

    sealed class Wish {
        object Apply : Wish()
        object Clear : Wish()
        data class ChangeFilterExternal(
            val newFilter: Filter
        ) : Wish()

        data class ChangeField(
            val fieldKey: FilterKey,
            val fieldValue: String?
        ) : Wish()
    }

    sealed class Effect {
        data class UpdateFilter(
            val filter: Filter
        ) : Effect()
        object FilterApplied: Effect()
    }

    sealed class News {
        object FilterApplied : News()
    }
}