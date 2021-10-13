package ru.ktsstudio.reo.domain.map.mno_info

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno

/**
 * Created by Igor Park on 05/10/2020.
 */
class MnoInfoFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>
) : BaseMviFeature<
    MnoInfoFeature.Wish,
    MnoInfoFeature.Effect,
    MnoInfoFeature.State,
    Nothing>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        bootstrapper = null,
        newsPublisher = null
    ) {

    data class State(
        val infoList: List<Pair<Mno, List<Measurement>>>,
        val isLoading: Boolean,
        val error: Throwable?
    )

    sealed class Wish {
        data class LoadData(val objectIds: List<String>) : Wish()
    }

    sealed class Effect {
        object Loading : Effect()
        data class Success(val infoList: List<Pair<Mno, List<Measurement>>>) : Effect()
        data class Error(override val throwable: Throwable) : Effect(), ErrorEffect
    }
}
