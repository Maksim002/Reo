package ru.ktsstudio.app_verification.domain.object_survey.general

import com.badoo.mvicore.element.NewsPublisher

/**
 * Created by Igor Park on 04/12/2020.
 */
internal class GeneralSurveyNewsPublisher : NewsPublisher<
    GeneralSurveyFeature.Wish,
    GeneralSurveyFeature.Effect,
    GeneralSurveyFeature.State,
    GeneralSurveyFeature.News
    > {

    override fun invoke(
        action: GeneralSurveyFeature.Wish,
        effect: GeneralSurveyFeature.Effect,
        state: GeneralSurveyFeature.State
    ): GeneralSurveyFeature.News? {
        return when (effect) {
            is GeneralSurveyFeature.Effect.SurveySaved -> GeneralSurveyFeature.News.Exit
            is GeneralSurveyFeature.Effect.Exit -> GeneralSurveyFeature.News.Exit
            is GeneralSurveyFeature.Effect.ShowExitDialog -> GeneralSurveyFeature.News.ShowExitDialog
            else -> null
        }
    }
}
