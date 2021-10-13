package ru.ktsstudio.app_verification.domain.object_survey.common

import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.core_data_verfication_api.data.model.Media

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
internal class ObjectSurveyReducer<DraftType> :
    Reducer<ObjectSurveyFeature.State<DraftType>, ObjectSurveyFeature.Effect<DraftType>> {
    override fun invoke(
        state: ObjectSurveyFeature.State<DraftType>,
        effect: ObjectSurveyFeature.Effect<DraftType>
    ): ObjectSurveyFeature.State<DraftType> {
        return when (effect) {
            is ObjectSurveyFeature.Effect.Loading -> state.copy(
                loading = true,
                error = null
            )
            is ObjectSurveyFeature.Effect.Error -> {
                state.copy(
                    loading = false,
                    error = effect.throwable
                )
            }
            is ObjectSurveyFeature.Effect.Success -> {
                state.copy(
                    loading = false,
                    error = null,
                    draft = effect.draft
                )
            }
            is ObjectSurveyFeature.Effect.SurveySet -> {
                state.copy(
                    draft = effect.draft
                )
            }
            is ObjectSurveyFeature.Effect.SurveyUpdated -> {
                state.copy(
                    draft = effect.draft,
                    draftUpdated = true
                )
            }
            is ObjectSurveyFeature.Effect.SurveyMediaDeleted -> {
                state.copy(
                    draft = effect.draft,
                    draftUpdated = effect.draftUpdated,
                    mediasToSave = effect.mediasToSave,
                    mediasToDelete = effect.mediasToDelete,
                    updatingMedia = effect.updatingMedia
                )
            }
            is ObjectSurveyFeature.Effect.CapturedMediaConsumerChanged -> {
                state.copy(
                    draft = effect.draftWithCancelledPreviousMedia,
                    updatingMedia = effect.newMediaUpdater
                )
            }
            is ObjectSurveyFeature.Effect.CapturingMediaReady -> {
                val mediaWithoutLocation = Media(
                    remoteId = null,
                    remoteUrl = null,
                    cachedFile = effect.capturingMedia.cachedFile,
                    gpsPoint = effect.capturingMedia.gpsPoint,
                    date = effect.capturingMedia.date
                )
                val mediaListWithStub = state.updatingMedia
                    ?.get()
                    .orEmpty() + mediaWithoutLocation

                val mediaUpdater = state.updatingMedia
                    ?.consume(mediaListWithStub)
                    as? ValueConsumer<List<Media>, DraftType>

                state.copy(
                    draft = state.draft?.let { draft ->
                        mediaUpdater?.update(draft) ?: draft
                    },
                    updatingMedia = mediaUpdater
                )
            }
            is ObjectSurveyFeature.Effect.MediaLocationReady -> {
                val surveyMediaUpdater = state.updatingMedia
                var mediaToSave: Media? = null
                state.copy(
                    draft = state.draft?.let { draft ->
                        surveyMediaUpdater?.consume(
                            surveyMediaUpdater.get()
                                .map { media ->
                                    if (media.cachedFile?.path == effect.mediaFile.path) {
                                        mediaToSave = media
                                        media.copy(gpsPoint = effect.mediaLocation)
                                    } else {
                                        media
                                    }
                                }
                        )
                            ?.update(draft)
                            ?: draft
                    },
                    mediasToSave = state.mediasToSave + listOfNotNull(mediaToSave),
                    updatingMedia = null
                )
            }

            is ObjectSurveyFeature.Effect.CancelledPhoto -> {
                state.copy(draft = effect.updatedDraft, updatingMedia = effect.updatingMedia)
            }
            is ObjectSurveyFeature.Effect.MediaUpdateIsNotCompleted,
            is ObjectSurveyFeature.Effect.GetLocationFailed,
            is ObjectSurveyFeature.Effect.DraftIsInvalid,
            is ObjectSurveyFeature.Effect.GpsAvailabilityState,
            is ObjectSurveyFeature.Effect.SurveySaved,
            is ObjectSurveyFeature.Effect.Exit,
            is ObjectSurveyFeature.Effect.ShowExitDialog -> state
        }
    }
}
