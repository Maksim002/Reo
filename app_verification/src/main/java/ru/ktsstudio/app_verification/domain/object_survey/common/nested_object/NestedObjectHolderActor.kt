package ru.ktsstudio.app_verification.domain.object_survey.common.nested_object

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */

typealias NestedObjectAdder<DraftType, NestedObjectType> = (DraftType, NestedObjectType) -> DraftType
typealias NestedObjectDeleter<DraftType, NestedObjectType> = (DraftType, String, NestedObjectType) -> DraftType

class NestedObjectHolderActor<DraftType, NestedObjectType>(
    private val adder: NestedObjectAdder<DraftType, NestedObjectType>,
    private val deleter: NestedObjectDeleter<DraftType, NestedObjectType>
) : Actor<
    NestedObjectHolderFeature.State<DraftType>,
    NestedObjectHolderFeature.Wish<DraftType, NestedObjectType>,
    NestedObjectHolderFeature.Effect<DraftType>> {
    override fun invoke(
        state: NestedObjectHolderFeature.State<DraftType>,
        action: NestedObjectHolderFeature.Wish<DraftType, NestedObjectType>
    ): Observable<out NestedObjectHolderFeature.Effect<DraftType>> {
        return when (action) {
            is NestedObjectHolderFeature.Wish.SetDraft<DraftType, NestedObjectType> -> {
                Observable.just(NestedObjectHolderFeature.Effect.DraftChanged(action.draft))
            }
            is NestedObjectHolderFeature.Wish.Add -> {
                state.draft ?: error("cannot add element to nullable draft")
                Observable.just(
                    NestedObjectHolderFeature.Effect.DraftChanged(
                        adder(state.draft, action.objectType)
                    )
                )
            }
            is NestedObjectHolderFeature.Wish.Delete -> {
                state.draft ?: error("cannot delete element from nullable draft")
                Observable.just(
                    NestedObjectHolderFeature.Effect.DraftChanged(
                        deleter(state.draft, action.id, action.objectType)
                    )
                )
            }
        }
    }
}
