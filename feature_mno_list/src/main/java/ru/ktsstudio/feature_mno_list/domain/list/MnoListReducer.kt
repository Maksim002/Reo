package ru.ktsstudio.feature_mno_list.domain.list

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Ovchinnikov on 01.10.2020.
 */
internal class MnoListReducer : Reducer<MnoListFeature.State, MnoListFeature.Effect> {
    override fun invoke(
        state: MnoListFeature.State,
        effect: MnoListFeature.Effect
    ): MnoListFeature.State {
        return when (effect) {
            MnoListFeature.Effect.Loading -> state.copy(
                loading = true,
                error = null
            )
            is MnoListFeature.Effect.Error -> {
                state.copy(
                    loading = false,
                    error = effect.throwable
                )
            }
            is MnoListFeature.Effect.Success -> {
                state.copy(
                    loading = false,
                    error = null,
                    mnoList = effect.mnoList,
                    mnoIdToMeasurementsMap = effect.mnoIdToMeasurementsMap
                )
            }
            is MnoListFeature.Effect.UpdatingFilter -> {
                state.copy(
                    currentFilter = effect.filter
                )
            }
        }
    }
}