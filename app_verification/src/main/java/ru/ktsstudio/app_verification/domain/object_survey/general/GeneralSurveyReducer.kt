package ru.ktsstudio.app_verification.domain.object_survey.general

import com.badoo.mvicore.element.Reducer

/**
 * Created by Igor Park on 04/12/2020.
 */
internal class GeneralSurveyReducer : Reducer<
    GeneralSurveyFeature.State,
    GeneralSurveyFeature.Effect
    > {
    override fun invoke(
        state: GeneralSurveyFeature.State,
        effect: GeneralSurveyFeature.Effect
    ): GeneralSurveyFeature.State {
        return when (effect) {
            GeneralSurveyFeature.Effect.Loading -> state.copy(
                loading = true,
                error = null
            )
            is GeneralSurveyFeature.Effect.Error -> {
                state.copy(
                    loading = false,
                    error = effect.throwable
                )
            }
            is GeneralSurveyFeature.Effect.Success -> {
                state.copy(
                    loading = false,
                    error = null,
                    surveyDraft = effect.generalInformationDraft
                )
            }
            is GeneralSurveyFeature.Effect.SurveyUpdated -> {
                state.copy(
                    surveyDraft = effect.updatedDraft,
                    draftUpdated = true
                )
            }
            is GeneralSurveyFeature.Effect.SurveySaved,
            is GeneralSurveyFeature.Effect.Exit,
            is GeneralSurveyFeature.Effect.ShowExitDialog -> state
        }
    }
}
