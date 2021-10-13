package ru.ktsstudio.app_verification.domain.object_list

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.11.2020.
 */
class ObjectListFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>
) : BaseMviFeature<
    ObjectListFeature.Wish,
    ObjectListFeature.Effect,
    ObjectListFeature.State,
    Nothing>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        bootstrapper = null
    ) {

    data class State(
        val data: List<VerificationObject> = emptyList(),
        val loading: Boolean = false,
        val error: Throwable? = null,
        val currentFilter: Filter = Filter.EMPTY,
        val sort: ObjectSort = ObjectSort.CREATED_AT_DESCENDING
    )

    sealed class Wish {
        object Load : Wish()
        data class ChangeFilter(val newFilter: Filter) : Wish()
        data class ChangeSort(val newSort: ObjectSort) : Wish()
        data class ChangeSearchQuery(val searchQuery: String) : Wish()
    }

    sealed class Effect {
        object Loading : Effect()
        data class UpdatingFilter(
            val filter: Filter
        ) : Effect()
        data class UpdatingSort(val newSort: ObjectSort) : Effect()

        data class Success(
            val objectList: List<VerificationObject>
        ) : Effect()

        data class Error(
            override val throwable: Throwable
        ) : Effect(), ErrorEffect
    }
}
