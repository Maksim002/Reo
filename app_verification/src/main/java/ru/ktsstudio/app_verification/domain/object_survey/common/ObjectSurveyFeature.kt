package ru.ktsstudio.app_verification.domain.object_survey.common

import android.net.Uri
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.app_verification.domain.models.CapturingMedia
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import java.io.File

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
internal class ObjectSurveyFeature<DraftType>(
    initialState: State<DraftType>,
    actor: Actor<State<DraftType>, Wish<DraftType>, Effect<DraftType>>,
    reducer: Reducer<State<DraftType>, Effect<DraftType>>,
    newsPublisher: NewsPublisher<Wish<DraftType>, Effect<DraftType>, State<DraftType>, News<DraftType>>
) : BaseMviFeature<
    ObjectSurveyFeature.Wish<DraftType>,
    ObjectSurveyFeature.Effect<DraftType>,
    ObjectSurveyFeature.State<DraftType>,
    ObjectSurveyFeature.News<DraftType>>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher,
        bootstrapper = null
    ) {

    data class State<DraftType>(
        val draft: DraftType? = null,
        val draftUpdated: Boolean = false,
        val updatingMedia: ValueConsumer<List<Media>, DraftType>? = null,
        val mediasToSave: List<Media> = emptyList(),
        val mediasToDelete: List<Media> = emptyList(),
        val loading: Boolean = true,
        val error: Throwable? = null
    )

    sealed class Wish<DraftType> {
        data class Load<DraftType>(val objectId: String) : Wish<DraftType>()
        data class UpdateSurvey<DraftType>(val updater: Updater<DraftType>) : Wish<DraftType>()
        data class SetSurvey<DraftType>(val draft: DraftType) : Wish<DraftType>()
        data class DeleteSurveyMedia<DraftType>(
            val surveyWithDeletedMedia: Pair<ValueConsumer<List<Media>, DraftType>, Media>
        ) : Wish<DraftType>()

        class MediaCaptured<DraftType> : Wish<DraftType>()
        data class AddMedia<DraftType>(
            val updatingMediaSurvey: ValueConsumer<List<Media>, DraftType>
        ) : Wish<DraftType>()

        class CheckGpsState<DraftType> : Wish<DraftType>()
        class CaptureMediaRequest<DraftType> : Wish<DraftType>()
        data class SaveSurvey<DraftType>(val objectId: String) : Wish<DraftType>()
        data class CancelPhoto<DraftType>(val uri: Uri) : Wish<DraftType>()
        class Exit<DraftType> : Wish<DraftType>()
    }

    sealed class Effect<DraftType> {
        class Loading<DraftType> : Effect<DraftType>()
        data class Success<DraftType>(val draft: DraftType) : Effect<DraftType>()
        data class SurveySet<DraftType>(val draft: DraftType) : Effect<DraftType>()
        data class SurveyUpdated<DraftType>(val draft: DraftType) : Effect<DraftType>()
        class DraftIsInvalid<DraftType> : Effect<DraftType>()
        class MediaUpdateIsNotCompleted<DraftType> : Effect<DraftType>()
        data class SurveyMediaDeleted<DraftType>(
            val draft: DraftType?,
            val draftUpdated: Boolean,
            val mediasToSave: List<Media>,
            val mediasToDelete: List<Media>,
            val updatingMedia: ValueConsumer<List<Media>, DraftType>?
        ) : Effect<DraftType>()

        data class CapturedMediaConsumerChanged<DraftType>(
            val draftWithCancelledPreviousMedia: DraftType,
            val newMediaUpdater: ValueConsumer<List<Media>, DraftType>
        ) : Effect<DraftType>()

        data class GpsAvailabilityState<DraftType>(val gpsState: GpsState) : Effect<DraftType>()
        class SurveySaved<DraftType> : Effect<DraftType>()
        data class Error<DraftType>(override val throwable: Throwable) : Effect<DraftType>(), ErrorEffect
        data class CancelledPhoto<DraftType>(
            val updatedDraft: DraftType?,
            val updatingMedia: ValueConsumer<List<Media>, DraftType>?
        ) : Effect<DraftType>()

        data class CapturingMediaReady<DraftType>(val capturingMedia: CapturingMedia) : Effect<DraftType>()
        data class MediaLocationReady<DraftType>(val mediaFile: File, val mediaLocation: GpsPoint) : Effect<DraftType>()
        data class GetLocationFailed<DraftType>(val uri: Uri) : Effect<DraftType>()
        class Exit<DraftType> : Effect<DraftType>()
        class ShowExitDialog<DraftType> : Effect<DraftType>()
    }

    sealed class News<DraftType> {
        class Exit<DraftType> : News<DraftType>()
        class ShowExitDialog<DraftType> : News<DraftType>()
        class DraftIsInvalid<DraftType> : News<DraftType>()
        data class GpsAvailabilityState<DraftType>(val gpsState: GpsState) : News<DraftType>()
        data class CaptureMedia<DraftType>(val capturingMedia: CapturingMedia) : News<DraftType>()
        class CheckMediaPermission<DraftType> : News<DraftType>()
        data class CancelPhotoWithoutLocation<DraftType>(val uri: Uri) : News<DraftType>()
        class MediaUpdateIsNotCompleted<DraftType> : News<DraftType>()
    }
}
