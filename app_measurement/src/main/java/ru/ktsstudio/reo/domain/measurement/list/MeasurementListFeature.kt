package ru.ktsstudio.reo.domain.measurement.list

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
internal class MeasurementListFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>
) : BaseMviFeature<
    MeasurementListFeature.Wish,
    MeasurementListFeature.Effect,
    MeasurementListFeature.State,
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
        val data: List<Pair<Measurement, Mno>> = emptyList(),
        val currentFilter: Filter = Filter.EMPTY,
        val sort: MeasurementSort = MeasurementSort.STATUS
    )

    sealed class Wish {
        object Load : Wish()
        data class ChangeFilter(val newFilter: Filter) : Wish()
        data class ChangeSort(val newSort: MeasurementSort) : Wish()
        data class ChangeSearchQuery(val searchQuery: String) : Wish()
    }

    sealed class Effect {
        object Loading : Effect()

        data class UpdatingFilter(
            val filter: Filter
        ) : Effect()

        data class UpdatingSort(val newSort: MeasurementSort) : Effect()

        data class Success(
            val measurementWithMnoList: List<Pair<Measurement, Mno>>
        ) : Effect()

        data class Error(
            override val throwable: Throwable
        ) : Effect(), ErrorEffect
    }
}
