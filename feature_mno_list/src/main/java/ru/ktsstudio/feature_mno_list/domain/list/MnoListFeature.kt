package ru.ktsstudio.feature_mno_list.domain.list

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno

/**
 * @author Maxim Ovchinnikov on 30.09.2020.
 */
internal class MnoListFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>
) : BaseMviFeature<
        MnoListFeature.Wish,
        MnoListFeature.Effect,
        MnoListFeature.State,
        Nothing>(
    initialState = initialState,
    actor = actor,
    reducer = reducer,
    bootstrapper = null,
    newsPublisher = null
) {

    data class State(
        val loading: Boolean = true,
        val error: Throwable? = null,
        val mnoList: List<Mno> = emptyList(),
        val mnoIdToMeasurementsMap: Map<String, List<Measurement>> = emptyMap(),
        val currentFilter: Filter = Filter.EMPTY
    )

    sealed class Wish {
        object Load : Wish()
        data class ChangeFilter(val newFilter: Filter) : Wish()
        data class ChangeSearchQuery(val searchQuery: String) : Wish()
    }

    sealed class Effect {
        object Loading : Effect()
        data class UpdatingFilter(
            val filter: Filter
        ) : Effect()
        data class Success(
            val mnoList: List<Mno>,
            val mnoIdToMeasurementsMap: Map<String, List<Measurement>>
        ) : Effect()

        data class Error(
            override val throwable: Throwable
        ) : Effect(), ErrorEffect
    }
}