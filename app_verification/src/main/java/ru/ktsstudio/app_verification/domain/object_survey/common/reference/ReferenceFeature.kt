package ru.ktsstudio.app_verification.domain.object_survey.common.reference

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

internal class ReferenceFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>
) : BaseMviFeature<
    ReferenceFeature.Wish,
    ReferenceFeature.Effect,
    ReferenceFeature.State,
    Nothing>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = null,
        bootstrapper = null
    ) {

    data class State(
        val references: List<Reference> = emptyList(),
        val error: Throwable? = null,
        val loading: Boolean = true
    )

    sealed class Wish {
        object Load : Wish()
    }

    sealed class Effect {
        data class Success(val references: List<Reference>) : Effect()
        data class Error(override val throwable: Throwable) : Effect(), ErrorEffect
        object Loading : Effect()
    }
}
