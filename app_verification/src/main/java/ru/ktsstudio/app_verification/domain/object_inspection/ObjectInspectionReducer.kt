package ru.ktsstudio.app_verification.domain.object_inspection

import com.badoo.mvicore.element.Reducer

/**
 * @author Maxim Ovchinnikov on 21.11.2020.
 */
class ObjectInspectionReducer : Reducer<
    ObjectInspectionFeature.State,
    ObjectInspectionFeature.Effect
    > {
    override fun invoke(
        state: ObjectInspectionFeature.State,
        effect: ObjectInspectionFeature.Effect
    ): ObjectInspectionFeature.State {
        return when (effect) {
            ObjectInspectionFeature.Effect.Loading -> state.copy(
                isLoading = true,
                error = null
            )
            is ObjectInspectionFeature.Effect.Error -> {
                state.copy(
                    isLoading = false,
                    error = effect.throwable
                )
            }
            is ObjectInspectionFeature.Effect.Success -> {
                state.copy(
                    isLoading = false,
                    error = null,
                    surveyProgress = effect.surveyProgress
                )
            }

            is ObjectInspectionFeature.Effect.SendComplete,
            is ObjectInspectionFeature.Effect.SendFailed -> {
                state.copy(
                    isLoading = false,
                    isSending = false,
                    error = null
                )
            }

            is ObjectInspectionFeature.Effect.Sending -> {
                state.copy(
                    isLoading = false,
                    isSending = true,
                    error = null
                )
            }
        }
    }
}
