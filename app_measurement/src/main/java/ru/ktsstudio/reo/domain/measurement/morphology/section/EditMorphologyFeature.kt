package ru.ktsstudio.reo.domain.measurement.morphology.section

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem

/**
 * Created by Igor Park on 19/10/2020.
 */
class EditMorphologyFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>
) : BaseMviFeature<
    EditMorphologyFeature.Wish,
    EditMorphologyFeature.Effect,
    EditMorphologyFeature.State,
    Nothing
    >(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = null,
        bootstrapper = null
    ) {
    data class State(
        val morphologyList: List<MorphologyItem>,
        val isLoading: Boolean,
        val error: Throwable?
    )

    sealed class Wish {
        data class StartObservingMorphology(val measurementId: Long) : Wish()
    }

    sealed class Effect {
        data class DataChanged(val newMorphologyList: List<MorphologyItem>) : Effect()
        object DataLoading : Effect()
        data class DataLoadError(override val throwable: Throwable) : Effect(), ErrorEffect
    }
}
