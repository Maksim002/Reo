package ru.ktsstudio.reo.domain.measurement.create_measurement

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.utils.orDefault
import ru.ktsstudio.common.utils.rx.Rx3Completable
import ru.ktsstudio.common.utils.rx.Rx3Maybe
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.Rx3Single
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementComposite
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import ru.ktsstudio.reo.domain.measurement.common.MeasurementDraft
import ru.ktsstudio.reo.domain.measurement.common.MeasurementDraftHolder
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature.Effect
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature.State
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature.Wish
import ru.ktsstudio.utilities.extensions.zipWithTimer

/**
 * Created by Igor Park on 10/10/2020.
 */
class CreateMeasurementActor(
    private val measurementRepository: MeasurementRepository,
    private val locationRepository: LocationRepository,
    private val mediaRepository: MediaRepository,
    private val schedulers: SchedulerProvider,
    private val draftHolder: MeasurementDraftHolder
) : Actor<State, Wish, Effect> {
    private val interruptSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        val wishToEffect = when (action) {
            is Wish.StartObservingMeasurement -> startObserveMeasurement(
                mnoId = action.mnoId,
                measurementId = action.measurementLocalId
            )
            is Wish.CaptureMediaRequest -> prepareCapturingMedia(action)
            is Wish.MediaCaptured -> saveMediaForMeasurement(action, state)
            is Wish.DeleteMedia -> deleteMedia(action.mediaId)
            is Wish.ExitWithSaveCheck -> exitWithSaveCheck(state.measurement, state.isEditMode)
            is Wish.ClearData -> clearMeasurementAndExit(
                state.measurement?.localId,
                state.isEditMode
            )
            is Wish.CommentChanged -> {
                draftHolder.saveComment(action.comment)
                Rx3Observable.just(Effect.CommentChanged(action.comment))
            }
            is Wish.CheckGpsState -> getGpsState(state.gpsEnableRejected)
            is Wish.GpsEnableRejected -> Rx3Observable.just(Effect.GpsEnableRejected)
            is Wish.SetLocation -> setLocationForMeasurement(state)
        }

        return wishToEffect.observeOn(schedulers.ui)
            .toRx2Observable()
    }

    private fun prepareCapturingMedia(action: Wish.CaptureMediaRequest): Rx3Observable<Effect> {
        val fileName = if (action.category == MeasurementMediaCategory.VIDEO) {
            DEFAULT_VIDEO_FILE_NAME
        } else {
            DEFAULT_PHOTO_FILE_NAME
        }
        val cachedFile = mediaRepository.createFile(fileName)
        val capturingMedia = CapturingMedia(
            cachedFile = cachedFile,
            outputUri = mediaRepository.getUriForFile(cachedFile),
            measurementMediaCategory = action.category,
            withLocation = action.withLocation
        )
        return Rx3Observable.just(Effect.CapturingMediaReady(capturingMedia))
    }

    private fun saveMediaForMeasurement(
        action: Wish.MediaCaptured,
        state: State
    ): Rx3Observable<Effect> {
        val measurementId: Long = state.measurement!!.localId
        val capturingMedia: CapturingMedia = state.capturingMedia
            ?: error("$action triggered without capturing media")
        val category: MeasurementMediaCategory = capturingMedia.measurementMediaCategory
        return measurementRepository.createMediaForMeasurement(measurementId, category)
            .flatMapObservable { mediaLocalId ->
                updateMedia(
                    mediaLocalId = mediaLocalId,
                    capturingMedia = capturingMedia
                )
                    .andThen(
                        Rx3Observable.just<Effect>(
                            Effect.MediaProcessingStateChange(
                                mediaId = mediaLocalId,
                                processingState = MediaProcessingState.Completed
                            )
                        ).zipWithTimer(LOAD_DELAY)
                    )
                    .startWithItem(
                        Effect.MediaProcessingStateChange(
                            mediaId = mediaLocalId,
                            processingState = MediaProcessingState.Processing
                        )
                    )
                    .onErrorResumeNext { error ->
                        measurementRepository.deleteMediaWithRelations(mediaLocalId)
                            .andThen(Rx3Observable.error(error))
                    }
            }
            .onErrorReturn(Effect::AddMediaFailed)
    }

    private fun updateMedia(
        mediaLocalId: Long,
        capturingMedia: CapturingMedia
    ): Completable {
        return when (capturingMedia.measurementMediaCategory) {
            MeasurementMediaCategory.PHOTO,
            MeasurementMediaCategory.VIDEO -> {
                getCurrentLocation().flatMapCompletable { gps ->
                    measurementRepository.updateMedia(
                        mediaLocalId = mediaLocalId,
                        filePath = mediaRepository.getBasePathForMediaFile(capturingMedia.cachedFile),
                        location = gps.takeIf { it.isEmpty().not() },
                        date = Instant.now()
                    )
                }
            }
            MeasurementMediaCategory.ACT_PHOTO -> {
                measurementRepository.updateMedia(
                    mediaLocalId = mediaLocalId,
                    filePath = mediaRepository.getBasePathForMediaFile(capturingMedia.cachedFile),
                    date = Instant.now(),
                    location = null
                )
            }
        }
    }

    private fun startObserveMeasurement(
        mnoId: String,
        measurementId: Long?
    ): Rx3Observable<Effect> {
        stopObservingMeasurement()

        val isEditMode = measurementId != null

        fun observeMeasurement(): Rx3Observable<Effect> {
            val observeMeasurementAction = if (measurementId != null) {
                measurementRepository.observeMeasurementComposite(measurementId)
            } else {
                createAndObserveMeasurement(mnoId)
            }
            return Rx3Observable.combineLatest(
                observeMeasurementAction,
                draftHolder.observeMeasurementDraft(),
                BiFunction { measurement: MeasurementComposite, draft: MeasurementDraft ->
                    measurement.mergeWithDraft(draft)
                }
            )
                .takeUntil(interruptSignal)
                .withMeasurementProgressState(isEditMode = isEditMode)
        }

        fun rollbackPreviousSession(): Completable = if (isEditMode) {
            measurementRepository.rollbackChangedMeasurementEntities(measurementId!!)
        } else {
            Completable.complete()
        }

        return rollbackPreviousSession()
            .andThen(observeMeasurement())
    }

    private fun exitWithSaveCheck(
        measurement: MeasurementComposite?,
        isEditMode: Boolean
    ): Rx3Observable<Effect> {
        val measurementId = measurement?.localId
            ?: return Rx3Observable.just(Effect.ExitMeasurement)

        return when {
            isEditMode -> {
                measurementRepository.checkMeasurementForChanges(measurementId)
                    .flatMapObservable { hasChanged ->
                        if (hasChanged || draftHolder.isEmpty().not()) {
                            Rx3Observable.just<Effect>(Effect.ConfirmDataClear)
                        } else {
                            Rx3Observable.just<Effect>(Effect.ExitMeasurement)
                        }
                    }
            }
            else -> {
                if (measurement.isEmpty()) {
                    clearMeasurementAndExit(measurementId, isEditMode = false)
                } else {
                    Rx3Observable.just<Effect>(Effect.ConfirmDataClear)
                }
            }
        }
    }

    private fun deleteMedia(mediaId: Long): Rx3Observable<Effect> {
        return measurementRepository.setMediaDeleted(mediaId)
            .onErrorReturn<Effect>(Effect::DeleteMediaFailed)
            .toObservable()
    }

    private fun createAndObserveMeasurement(mnoId: String): Rx3Observable<MeasurementComposite> {
        return measurementRepository.createAndObserveMeasurementComposite(
            mnoId = mnoId,
            gpsPoint = GpsPoint.EMPTY,
            date = Instant.now()
        )
    }

    private fun stopObservingMeasurement() {
        interruptSignal.onNext(Unit)
    }

    private fun setLocationForMeasurement(state: State): Rx3Observable<Effect> {
        val measurementId = state.measurement?.localId ?: error("Measurement is null")
        return getCurrentLocation()
            .flatMapCompletable { gps ->
                measurementRepository.updateLocationForMeasurement(measurementId, gps)
            }
            .andThen(validateDailyGain(state))
            .andThen(Rx3Observable.just<Effect>(Effect.SetLocationCompleted))
            .startWithItem(Effect.Loading)
            .onErrorReturn(Effect::SetLocationFailed)
    }

    private fun validateDailyGain(state: State): Rx3Completable {
        if (state.measurement != null && state.measurement.morphologyItemList.isEmpty()) return Completable.complete()

        val morphologyVolumeGain = state.measurement?.morphologyItemList
            .orEmpty()
            .map { it.dailyGainVolume.orDefault(0f) }
            .sum()

        val morphologyWeightGain = state.measurement?.morphologyItemList
            .orEmpty()
            .map { it.dailyGainWeight.orDefault(0f) }
            .sum()

        val draftSeparateContainers = draftHolder.getSessionMeasurementDraft().separateContainers
        val draftMixedContainers = draftHolder.getSessionMeasurementDraft().mixedContainers

        val draftContainerVolumeGain =
            draftMixedContainers.sumByDouble { it.dailyGainVolume.toDouble() } +
                draftSeparateContainers.flatMap { it.wasteTypes }
                    .sumByDouble { it.dailyGainVolume.toDouble() }
        val draftContainerWeightGain =
            draftMixedContainers.sumByDouble { it.dailyGainNetWeight.toDouble() } +
                draftSeparateContainers.flatMap { it.wasteTypes }
                    .sumByDouble { it.dailyGainNetWeight.toDouble() }

        val (separateContainers, mixedContainers) = state.measurement
            ?.containers
            .orEmpty()
            .partition { it.isSeparate }

        val separateContainersSingle = Rx3Maybe.merge(
            separateContainers.map { measurementRepository.getSeparateWasteContainerById(it.id) }
        ).toList()

        val mixedContainersSingle = Rx3Maybe.merge(
            mixedContainers.map { measurementRepository.getMixedWasteContainerById(it.id) }
        ).toList()

        return Rx3Single.zip(
            separateContainersSingle,
            mixedContainersSingle,
            BiFunction {
                    compositeSeparateContainers: List<SeparateContainerComposite>,
                    compositeMixedContainers: List<MixedWasteContainerComposite> ->
                val containerVolumeGain =
                    compositeMixedContainers.sumByDouble { it.dailyGainVolume.toDouble() } +
                        compositeSeparateContainers.flatMap { it.wasteTypes }
                            .sumByDouble { it.dailyGainVolume.toDouble() }
                val containerWeightGain =
                    compositeMixedContainers.sumByDouble { it.dailyGainNetWeight.toDouble() } +
                        compositeSeparateContainers.flatMap { it.wasteTypes }
                            .sumByDouble { it.dailyGainNetWeight.toDouble() }

                containerVolumeGain + draftContainerVolumeGain to containerWeightGain + draftContainerWeightGain
            }
        )
            .flatMapCompletable { (containerVolumeGain, containerWeightGain) ->
                if (containerVolumeGain < morphologyVolumeGain || containerWeightGain < morphologyWeightGain) {
                    Rx3Completable.error(IncorrectGainException())
                } else {
                    Rx3Completable.complete()
                }
            }
    }

    private fun getGpsState(gpsEnableRejected: Boolean): Rx3Observable<Effect> {
        return if (gpsEnableRejected) {
            Rx3Observable.just(Effect.GpsAvailabilityState(GpsState.REJECTED))
        } else {
            locationRepository.checkIfGpsEnabled()
                .flatMapObservable { gpsEnabled ->
                    val gpsState = if (gpsEnabled) GpsState.ENABLED else GpsState.DISABLED
                    Rx3Observable.just(Effect.GpsAvailabilityState(gpsState))
                }
        }
    }

    private fun getCurrentLocation(): Single<GpsPoint> {
        return locationRepository.getCurrentLocation()
            .toSingle()
            .onErrorReturnItem(GpsPoint.EMPTY)
    }

    private fun clearMeasurementAndExit(
        measurementId: Long?,
        isEditMode: Boolean
    ): Rx3Observable<Effect> {
        if (measurementId == null) return Rx3Observable.just(Effect.ExitMeasurement)

        val clearCompletable = if (isEditMode) {
            measurementRepository.rollbackChangedMeasurementEntities(measurementId)
        } else {
            measurementRepository.deleteMeasurementById(measurementId)
        }

        return clearCompletable
            .andThen(Rx3Observable.just<Effect>(Effect.ExitMeasurement))
            .startWithItem(Effect.Loading)
            .onErrorReturn(Effect::LoadingFailed)
    }

    private fun Rx3Observable<MeasurementComposite>.withMeasurementProgressState(
        isEditMode: Boolean
    ): Rx3Observable<Effect> {
        return map<Effect>(Effect::MeasurementChanged)
            .startWithIterable(
                listOf(Effect.Loading, Effect.EditModeCheck(isEditMode))
            )
            .onErrorReturn(Effect::LoadingFailed)
            .observeOn(schedulers.ui)
    }

    private fun MeasurementComposite.mergeWithDraft(draft: MeasurementDraft): MeasurementComposite {
        return this.copy(
            containers = containers.map { container ->
                if (container.isSeparate) {
                    val separateContainerDraft = draft.separateContainers
                        .find { it.localId == container.id }

                    mergeSeparateContainers(
                        container = container,
                        update = separateContainerDraft
                    )
                } else {
                    val mixedContainerDraft = draft.mixedContainers
                        .find { container.id == it.localId }

                    mergeMixedContainers(
                        container = container,
                        update = mixedContainerDraft
                    )
                }
            },
            morphologyItemList = morphologyItemList.map { morphologyItem ->
                val changedMorphology = draft.morphologyItems
                    .find { it.localId == morphologyItem.localId }

                mergeMorphology(
                    morphology = morphologyItem,
                    update = changedMorphology
                )
            }
        )
    }

    private fun mergeSeparateContainers(
        container: MeasurementComposite.Container,
        update: SeparateContainerComposite?
    ): MeasurementComposite.Container {
        return container.copy(
            name = update?.getName() ?: container.name,
            volume = update?.getVolume() ?: container.volume
        )
    }

    private fun mergeMixedContainers(
        container: MeasurementComposite.Container,
        update: MixedWasteContainerComposite?
    ): MeasurementComposite.Container {
        return container.copy(
            name = update?.getName() ?: container.name,
            volume = update?.getVolume() ?: container.volume
        )
    }

    private fun mergeMorphology(
        morphology: MorphologyItem,
        update: MorphologyItem?
    ): MorphologyItem {
        return update?.copy(localId = morphology.localId)
            ?: morphology
    }

    companion object {
        private const val DEFAULT_PHOTO_FILE_NAME = "photo.jpg"
        private const val DEFAULT_VIDEO_FILE_NAME = "video.mp4"
        private const val LOAD_DELAY = 700L
    }
}
