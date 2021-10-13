package ru.ktsstudio.common.domain.filter.data

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterActor
import ru.ktsstudio.common.domain.filter.FilterBootstrapper
import ru.ktsstudio.common.domain.filter.FilterFeature
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.domain.filter.FilterNewsPublisher
import ru.ktsstudio.common.domain.filter.FilterReducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class FilterDataFeature<DataType>(
    initialState: State<DataType>,
    actor: FilterDataActor<DataType>,
    reducer: FilterDataReducer<DataType>
) : BaseMviFeature<
    FilterDataFeature.Wish,
    FilterDataFeature.Effect<DataType>,
    FilterDataFeature.State<DataType>,
    Nothing>(
    initialState = initialState,
    actor = actor,
    reducer = reducer,
    bootstrapper = null,
    newsPublisher = null,
) {

    data class State<DataType>(
        val data: Map<FilterKey, List<DataType>> = emptyMap()
    )

    sealed class Wish {
        object Load : Wish()
    }

    sealed class Effect<DataType> {
        data class ChangeData<DataType>(
            val key: FilterKey,
            val data: List<DataType>
        ) : Effect<DataType>()
    }
}