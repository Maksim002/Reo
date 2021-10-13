package ru.ktsstudio.reo.domain.measurement.list

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
internal class MeasurementListReducer : Reducer<MeasurementListFeature.State, MeasurementListFeature.Effect> {
    override fun invoke(
        state: MeasurementListFeature.State,
        effect: MeasurementListFeature.Effect
    ): MeasurementListFeature.State {
        return when (effect) {
            MeasurementListFeature.Effect.Loading -> state.copy(
                loading = true,
                error = null
            )
            is MeasurementListFeature.Effect.Error -> {
                state.copy(
                    loading = false,
                    error = effect.throwable
                )
            }
            is MeasurementListFeature.Effect.Success -> {
                state.copy(
                    loading = false,
                    error = null,
                    data = effect.measurementWithMnoList
                )
            }
            is MeasurementListFeature.Effect.UpdatingFilter -> {
                state.copy(
                    currentFilter = effect.filter
                )
            }
            is MeasurementListFeature.Effect.UpdatingSort -> {
                state.copy(
                    sort = effect.newSort
                )
            }
        }
    }
}
