package ru.ktsstudio.app_verification.domain.object_survey.common

import com.badoo.mvicore.element.NewsPublisher

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
internal class ObjectSurveyNewsPublisher<DraftType> :
    NewsPublisher<
        ObjectSurveyFeature.Wish<DraftType>,
        ObjectSurveyFeature.Effect<DraftType>,
        ObjectSurveyFeature.State<DraftType>,
        ObjectSurveyFeature.News<DraftType>
        > {

    override fun invoke(
        action: ObjectSurveyFeature.Wish<DraftType>,
        effect: ObjectSurveyFeature.Effect<DraftType>,
        state: ObjectSurveyFeature.State<DraftType>
    ): ObjectSurveyFeature.News<DraftType>? {
        return when (effect) {
            is ObjectSurveyFeature.Effect.SurveySaved -> ObjectSurveyFeature.News.Exit()
            is ObjectSurveyFeature.Effect.DraftIsInvalid -> ObjectSurveyFeature.News.DraftIsInvalid()
            is ObjectSurveyFeature.Effect.GpsAvailabilityState -> ObjectSurveyFeature.News.GpsAvailabilityState(
                effect.gpsState
            )
            is ObjectSurveyFeature.Effect.CapturingMediaReady -> ObjectSurveyFeature.News.CaptureMedia(
                effect.capturingMedia
            )
            is ObjectSurveyFeature.Effect.CapturedMediaConsumerChanged -> {
                ObjectSurveyFeature.News.CheckMediaPermission()
            }
            is ObjectSurveyFeature.Effect.GetLocationFailed -> ObjectSurveyFeature.News.CancelPhotoWithoutLocation(
                effect.uri
            )
            is ObjectSurveyFeature.Effect.MediaUpdateIsNotCompleted -> {
                ObjectSurveyFeature.News.MediaUpdateIsNotCompleted()
            }
            is ObjectSurveyFeature.Effect.Exit -> ObjectSurveyFeature.News.Exit()
            is ObjectSurveyFeature.Effect.ShowExitDialog -> ObjectSurveyFeature.News.ShowExitDialog()
            else -> null
        }
    }
}
