package ru.ktsstudio.app_verification.domain.object_list

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.11.2020.
 */
class ObjectListReducer : Reducer<ObjectListFeature.State, ObjectListFeature.Effect> {
    override fun invoke(
        state: ObjectListFeature.State,
        effect: ObjectListFeature.Effect
    ): ObjectListFeature.State {
        return when (effect) {
            ObjectListFeature.Effect.Loading -> state.copy(
                loading = true,
                error = null
            )
            is ObjectListFeature.Effect.Error -> {
                state.copy(
                    loading = false,
                    error = effect.throwable
                )
            }
            is ObjectListFeature.Effect.Success -> {
                state.copy(
                    loading = false,
                    error = null,
                    data = effect.objectList
                )
            }
            is ObjectListFeature.Effect.UpdatingFilter -> {
                state.copy(
                    currentFilter = effect.filter
                )
            }
            is ObjectListFeature.Effect.UpdatingSort -> {
                state.copy(
                    sort = effect.newSort
                )
            }
        }
    }
}
