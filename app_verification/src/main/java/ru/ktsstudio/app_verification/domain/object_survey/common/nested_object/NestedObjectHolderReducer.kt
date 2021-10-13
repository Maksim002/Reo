package ru.ktsstudio.app_verification.domain.object_survey.common.nested_object

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class NestedObjectHolderReducer<DraftType> : Reducer<
    NestedObjectHolderFeature.State<DraftType>,
    NestedObjectHolderFeature.Effect<DraftType>> {
    override fun invoke(
        state: NestedObjectHolderFeature.State<DraftType>,
        effect: NestedObjectHolderFeature.Effect<DraftType>
    ): NestedObjectHolderFeature.State<DraftType> {
        return when (effect) {
            is NestedObjectHolderFeature.Effect.DraftChanged -> state.copy(
                draft = effect.draft
            )
        }
    }
}
