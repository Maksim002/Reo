package ru.ktsstudio.feature_mno_list.domain.details

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.MixedWasteContainer
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.SeparateWasteContainer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 03.10.2020.
 */
internal class MnoDetailsFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>
) : BaseMviFeature<
        MnoDetailsFeature.Wish,
        MnoDetailsFeature.Effect,
        MnoDetailsFeature.State,
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
        val mno: Mno? = null,
        val measurements: List<Measurement> = emptyList()
    )

    sealed class Wish {
        data class Load(val id: String) : Wish()
    }

    sealed class Effect {
        object Loading : Effect()

        data class Success(
            val mnoDetails: Mno,
            val mnoMeasurements: List<Measurement>
        ) : Effect()

        data class Error(
            override val throwable: Throwable
        ) : Effect(), ErrorEffect
    }
}