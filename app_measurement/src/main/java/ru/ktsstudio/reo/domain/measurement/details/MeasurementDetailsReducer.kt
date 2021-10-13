package ru.ktsstudio.reo.domain.measurement.details

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
internal class MeasurementDetailsReducer :
    Reducer<MeasurementDetailsFeature.State, MeasurementDetailsFeature.Effect> {
    override fun invoke(
        state: MeasurementDetailsFeature.State,
        effect: MeasurementDetailsFeature.Effect
    ): MeasurementDetailsFeature.State {
        return when (effect) {
            MeasurementDetailsFeature.Effect.Loading -> state.copy(
                loading = true,
                error = null
            )
            is MeasurementDetailsFeature.Effect.Error -> {
                state.copy(
                    loading = false,
                    error = effect.throwable
                )
            }
            is MeasurementDetailsFeature.Effect.Success -> {
                state.copy(
                    loading = false,
                    error = null,
                    data = effect.measurementDetails,
                    isEditable = effect.isEditable
                )
            }
            is MeasurementDetailsFeature.Effect.PreviewModeInfo -> {
                state.copy(isPreviewMode = effect.isPreview)
            }

            is MeasurementDetailsFeature.Effect.MeasurementCreateInProcess -> {
                state.copy(
                    loading = false,
                    isCreating = true,
                    error = null
                )
            }
            is MeasurementDetailsFeature.Effect.MeasurementCreateFailed,
            is MeasurementDetailsFeature.Effect.MeasurementCreated -> state.copy(
                loading = false,
                isCreating = false,
                error = null
            )

            is MeasurementDetailsFeature.Effect.OpenEditMeasurement -> state
        }
    }
}
