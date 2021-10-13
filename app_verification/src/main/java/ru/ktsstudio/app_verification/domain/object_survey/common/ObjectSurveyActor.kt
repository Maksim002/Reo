package ru.ktsstudio.app_verification.domain.object_survey.common

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject
import org.threeten.bp.Instant
import ru.ktsstudio.app_verification.domain.models.CapturingMedia
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey
import ru.ktsstudio.utilities.extensions.zipWithTimer

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */

typealias DraftFactory<DraftType> = (VerificationObjectWithCheckedSurvey) -> DraftType
typealias VerificationObjectDraftMerger<DraftType> =
        (VerificationObjectWithCheckedSurvey, DraftType) -> VerificationObjectWithCheckedSurvey

typealias SurveyDraftValidator<DraftType> = (DraftType) -> Boolean

internal class ObjectSurveyActor<DraftType>(
    private val objectRepository: ObjectRepository,
    private val locationRepository: LocationRepository,
    private val mediaRepository: MediaRepository,
    private val schedulerProvider: SchedulerProvider,
    private val draftFactory: DraftFactory<DraftType>,
    private val verificationObjectDraftMerger: VerificationObjectDraftMerger<DraftType>,
    private val draftValidator: SurveyDraftValidator<DraftType> = { true }
) : Actor<ObjectSurveyFeature.State<DraftType>,
    ObjectSurveyFeature.Wish<DraftType>,
    ObjectSurveyFeature.Effect<DraftType>> {

    private val interruptLocationFetch = PublishSubject.create<Unit>()
    private val mediaCapturedSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: ObjectSurveyFeature.State<DraftType>,
        action: ObjectSurveyFeature.Wish<DraftType>
    ): Observable<ObjectSurveyFeature.Effect<DraftType>> {
        val observable: Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> = when (action) {
            is ObjectSurveyFeature.Wish.Load -> observeScheduleSurvey(action.objectId)
            is ObjectSurveyFeature.Wish.UpdateSurvey -> {
                val currentDraft = state.draft
                    ?: error("Action: $action, draft is not initialized!")
                Rx3Observable.just(
                    ObjectSurveyFeature.Effect.SurveyUpdated(
                        action.updater.update(currentDraft)
                    )
                )
            }
            is ObjectSurveyFeature.Wish.DeleteSurveyMedia -> prepareDeleteMediaEffect(state, action)
            is ObjectSurveyFeature.Wish.AddMedia -> processAddMediaRequest(action.updatingMediaSurvey, state)
            is ObjectSurveyFeature.Wish.MediaCaptured -> processMediaCapturedSignal()
            is ObjectSurveyFeature.Wish.CancelPhoto -> prepareCancelledPhotoEffect(action, state)
            is ObjectSurveyFeature.Wish.CheckGpsState -> getGpsState()
            is ObjectSurveyFeature.Wish.CaptureMediaRequest -> prepareCapturingMedia()
            is ObjectSurveyFeature.Wish.SaveSurvey -> {
                checkValidityAndSave(
                    objectId = action.objectId,
                    isMediaUpdateCompleted = state.updatingMedia == null,
                    draft = state.draft ?: error("draft is null"),
                    mediasToSave = state.mediasToSave,
                    mediasToDelete = state.mediasToDelete
                )
            }
            is ObjectSurveyFeature.Wish.SetSurvey -> {
                Rx3Observable.just(
                    ObjectSurveyFeature.Effect.SurveySet(action.draft)
                )
            }
            is ObjectSurveyFeature.Wish.Exit -> {
                if (state.draftUpdated) {
                    Rx3Observable.just(ObjectSurveyFeature.Effect.ShowExitDialog())
                } else {
                    Rx3Observable.just(ObjectSurveyFeature.Effect.Exit())
                }
            }
        }
        return observable.observeOn(schedulerProvider.ui).toRx2Observable()
    }

    private fun observeScheduleSurvey(objectId: String): Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> {
        return objectRepository.getObjectWithCheckedSurvey(objectId)
            .zipWithTimer(DELAY)
            .toObservable()
            .map<ObjectSurveyFeature.Effect<DraftType>> {
                ObjectSurveyFeature.Effect.Success(draft = draftFactory(it))
            }
            .startWithItem(ObjectSurveyFeature.Effect.Loading())
            .onErrorReturn { ObjectSurveyFeature.Effect.Error(it) }
    }

    private fun getGpsState(): Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> {
        return locationRepository.checkIfGpsEnabled()
            .flatMapObservable { gpsEnabled ->
                val gpsState = if (gpsEnabled) GpsState.ENABLED else GpsState.DISABLED
                Rx3Observable.just<ObjectSurveyFeature.Effect<DraftType>>(
                    ObjectSurveyFeature.Effect.GpsAvailabilityState(gpsState)
                )
            }
    }

    private fun prepareCapturingMedia(): Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> {
        val cachedFile = mediaRepository.createFile(DEFAULT_PHOTO_FILE_NAME)
        val fileUri = mediaRepository.getUriForFile(cachedFile)
        return locationRepository.getCurrentLocation().toSingle()
            .zipWith(
                mediaCapturedSignal.firstOrError(),
                BiFunction { gps: GpsPoint, _: Unit -> gps }
            )
            .zipWithTimer(DELAY)
            .toObservable()
            .takeUntil(interruptLocationFetch)
            .startWithItem(GpsPoint.EMPTY)
            .map<ObjectSurveyFeature.Effect<DraftType>> { gpsPoint ->
                if (gpsPoint.isEmpty()) {
                    ObjectSurveyFeature.Effect.CapturingMediaReady(
                        CapturingMedia(
                            outputUri = fileUri,
                            cachedFile = cachedFile,
                            gpsPoint = GpsPoint.EMPTY,
                            date = Instant.now()
                        )
                    )
                } else {
                    ObjectSurveyFeature.Effect.MediaLocationReady(
                        mediaFile = cachedFile,
                        mediaLocation = gpsPoint
                    )
                }
            }
            .onErrorResumeWith {
                Rx3Observable.fromCallable {
                    cachedFile.delete()
                    ObjectSurveyFeature.Effect.GetLocationFailed<DraftType>(fileUri)
                }
            }
    }

    private fun prepareDeleteMediaEffect(
        state: ObjectSurveyFeature.State<DraftType>,
        deleteAction: ObjectSurveyFeature.Wish.DeleteSurveyMedia<DraftType>
    ): Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> {
        val newMediaUpdater = deleteAction.surveyWithDeletedMedia.first
        val mediaToDelete = deleteAction.surveyWithDeletedMedia.second
        val oldMediasToSave = state.mediasToSave
        val mediasToDelete = state.mediasToDelete.toMutableList()

        val updatedSurveyMediaList = newMediaUpdater.get()
            .filter { it.id != mediaToDelete.id }
        val updatedMediasToSave = oldMediasToSave
            .filter { it.id != mediaToDelete.id }

        val isDeletingAlreadySavedMedia = updatedMediasToSave.size == state.mediasToSave.size
        if (isDeletingAlreadySavedMedia) mediasToDelete.add(mediaToDelete)
        val isDeletingMediaInProcess = mediaToDelete.isCapturedWithoutLocation()
        if (isDeletingMediaInProcess) stopFetchingLocation()

        return ObjectSurveyFeature.Effect.SurveyMediaDeleted(
            draft = state.draft?.let {
                newMediaUpdater.consume(updatedSurveyMediaList).update(it)
            },
            draftUpdated = true,
            mediasToSave = updatedMediasToSave,
            mediasToDelete = mediasToDelete,
            updatingMedia = if (areMediaUpdatersTheSame(newMediaUpdater, state.updatingMedia)) {
                newMediaUpdater.consume(updatedSurveyMediaList) as ValueConsumer<List<Media>, DraftType>
            } else {
                state.updatingMedia
            }
                .takeIf { isDeletingMediaInProcess.not() }
        ).let { Rx3Observable.just(it) }
    }

    private fun prepareCancelledPhotoEffect(
        cancelAction: ObjectSurveyFeature.Wish.CancelPhoto<DraftType>,
        state: ObjectSurveyFeature.State<DraftType>
    ): Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> {
        stopFetchingLocation()
        val mediaUpdater = state.updatingMedia
        val newMediaList = mediaUpdater?.get()
            .orEmpty()
            .filter { it.cachedFile?.name != cancelAction.uri.lastPathSegment }

        val updatedDraft = state.draft?.let { draft ->
            mediaUpdater?.consume(newMediaList)
                ?.update(draft)
                ?: draft
        }

        return Rx3Observable.just(ObjectSurveyFeature.Effect.CancelledPhoto(updatedDraft, null))
    }

    private fun processMediaCapturedSignal(): Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> {
        mediaCapturedSignal.onNext(Unit)
        return Rx3Observable.empty<ObjectSurveyFeature.Effect<DraftType>>()
    }

    private fun processAddMediaRequest(
        newMediaUpdater: ValueConsumer<List<Media>, DraftType>,
        state: ObjectSurveyFeature.State<DraftType>
    ): Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> {
        stopFetchingLocation()
        return Rx3Observable.fromCallable<ObjectSurveyFeature.Effect<DraftType>> {
            val mediasWithLocation = state.updatingMedia?.get()
                .orEmpty()
                .filter { media ->
                    media.isCapturedWithoutLocation().not()
                        .also { withLocation -> if (withLocation.not()) media.cachedFile?.delete() }
                }

            fun clearDraftFromUpdatingMedia(draft: DraftType?): DraftType {
                draft ?: error("Draft is null")

                return state.updatingMedia?.consume(mediasWithLocation)
                    ?.update(draft)
                    ?: draft
            }
            ObjectSurveyFeature.Effect.CapturedMediaConsumerChanged(
                draftWithCancelledPreviousMedia = clearDraftFromUpdatingMedia(state.draft),
                newMediaUpdater = if (areMediaUpdatersTheSame(newMediaUpdater, state.updatingMedia)) {
                    newMediaUpdater.consume(mediasWithLocation) as ValueConsumer<List<Media>, DraftType>
                } else {
                    newMediaUpdater
                }
            )
        }
            .subscribeOn(schedulerProvider.io)
    }

    private fun checkValidityAndSave(
        objectId: String,
        draft: DraftType,
        isMediaUpdateCompleted: Boolean,
        mediasToSave: List<Media>,
        mediasToDelete: List<Media>
    ): Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> {
        val isDraftValid = draftValidator.invoke(draft)
        return when {
            isDraftValid && isMediaUpdateCompleted -> {
                saveSurvey(objectId, draft, mediasToSave, mediasToDelete)
            }
            isDraftValid.not() -> {
                Rx3Observable.just(ObjectSurveyFeature.Effect.DraftIsInvalid())
            }
            isMediaUpdateCompleted.not() -> {
                Rx3Observable.just(ObjectSurveyFeature.Effect.MediaUpdateIsNotCompleted())
            }
            else -> error("Unreachable condition")
        }
    }

    private fun saveSurvey(
        objectId: String,
        draft: DraftType,
        mediasToSave: List<Media>,
        mediasToDelete: List<Media>
    ): Rx3Observable<ObjectSurveyFeature.Effect<DraftType>> {
        return objectRepository.getObjectWithCheckedSurvey(objectId)
            .map { verificationObjectDraftMerger(it, draft) }
            .flatMapCompletable {
                objectRepository.saveObjectWithCheckedSurvey(it)
                    .andThen(objectRepository.deleteObjectMedias(mediasToDelete))
                    .andThen(objectRepository.saveObjectMedias(mediasToSave))
            }
            .andThen(
                Rx3Observable.just<ObjectSurveyFeature.Effect<DraftType>>(
                    ObjectSurveyFeature.Effect.SurveySaved()
                )
            )
            .onErrorReturn { ObjectSurveyFeature.Effect.Error(it) }
    }

    private fun stopFetchingLocation() = interruptLocationFetch.onNext(Unit)

    private fun Media.isCapturedWithoutLocation(): Boolean {
        return cachedFile != null && gpsPoint.isEmpty()
    }

    private fun areMediaUpdatersTheSame(
        updaterFirst: ValueConsumer<List<Media>, DraftType>?,
        updaterSecond: ValueConsumer<List<Media>, DraftType>?
    ): Boolean {
        if (updaterFirst == null || updaterSecond == null) return false
        return updaterFirst.get() == updaterSecond.get()
    }

    companion object {
        private const val DEFAULT_PHOTO_FILE_NAME = "photo.jpg"
        private const val DELAY = 500L
    }
}
