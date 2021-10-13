package ru.ktsstudio.app_verification.domain.object_survey.common.nested_object

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class NestedObjectHolderFeature<DraftType, NestedObjectType>(
    initialState: State<DraftType>,
    actor: Actor<State<DraftType>, Wish<DraftType, NestedObjectType>, Effect<DraftType>>,
    reducer: Reducer<State<DraftType>, Effect<DraftType>>
) : BaseMviFeature<
    NestedObjectHolderFeature.Wish<DraftType, NestedObjectType>,
    NestedObjectHolderFeature.Effect<DraftType>,
    NestedObjectHolderFeature.State<DraftType>,
    NestedObjectHolderFeature.News>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = null,
        bootstrapper = null
    ) {

    data class State<DraftType>(
        val draft: DraftType? = null,
    )

    sealed class Wish<DraftType, NestedObjectType> {
        data class Add<DraftType, NestedObjectType>(
            val objectType: NestedObjectType
        ) : Wish<DraftType, NestedObjectType>()

        data class Delete<DraftType, NestedObjectType>(
            val id: String,
            val objectType: NestedObjectType
        ) : Wish<DraftType, NestedObjectType>()

        data class SetDraft<DraftType, NestedObjectType>(
            val draft: DraftType
        ) : Wish<DraftType, NestedObjectType>()
    }

    sealed class Effect<DataType> {
        data class DraftChanged<DraftType>(
            val draft: DraftType
        ) : Effect<DraftType>()
    }

    sealed class News
}
