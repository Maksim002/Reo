package ru.ktsstudio.reo.domain.measurement.create_measurement

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.data.models.GpsState
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementComposite
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory

/**
 * Created by Igor Park on 10/10/2020.
 */

class CreateMeasurementFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    CreateMeasurementFeature.Wish,
    CreateMeasurementFeature.Effect,
    CreateMeasurementFeature.State,
    CreateMeasurementFeature.News
    >(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher,
        bootstrapper = null
    ) {
    data class State(
        val measurement: MeasurementComposite?,
        val isEditMode: Boolean,
        val capturingMedia: CapturingMedia?,
        val fileProcessingState: Map<Long, MediaProcessingState>,
        val isLoading: Boolean,
        val error: Throwable?,
        val gpsEnableRejected: Boolean
    )

    sealed class Wish {
        data class StartObservingMeasurement(
            val mnoId: String,
            val measurementLocalId: Long?
        ) : Wish()

        data class CommentChanged(val comment: String) : Wish()
        object ExitWithSaveCheck : Wish()
        object SetLocation : Wish()

        object ClearData : Wish()
        data class DeleteMedia(val mediaId: Long) : Wish()

        data class CaptureMediaRequest(
            val category: MeasurementMediaCategory,
            val withLocation: Boolean
        ) : Wish()

        object MediaCaptured : Wish()
        object GpsEnableRejected : Wish()
        object CheckGpsState : Wish()
    }

    sealed class Effect {
        data class MeasurementChanged(val measurement: MeasurementComposite) : Effect()
        data class CommentChanged(val comment: String) : Effect()
        data class MediaProcessingStateChange(
            val mediaId: Long,
            val processingState: MediaProcessingState
        ) : Effect()

        object SetLocationCompleted : Effect()
        data class SetLocationFailed(override val throwable: Throwable) : Effect(), ErrorEffect
        data class CapturingMediaReady(val capturingMedia: CapturingMedia) : Effect()

        data class AddMediaFailed(override val throwable: Throwable) : Effect(), ErrorEffect
        data class DeleteMediaFailed(override val throwable: Throwable) : Effect(), ErrorEffect
        object Loading : Effect()
        data class EditModeCheck(val isEditMode: Boolean) : Effect()

        data class GpsAvailabilityState(val state: GpsState) : Effect()
        object GpsEnableRejected : Effect()
        data class LoadingFailed(override val throwable: Throwable) : Effect(), ErrorEffect
        object ConfirmDataClear : Effect()
        object ExitMeasurement : Effect()
    }

    sealed class News {
        data class SetLocationFailed(val throwable: Throwable) : News()
        object SetLocationCompleted : News()
        data class CaptureMedia(val capturingMedia: CapturingMedia) : News()
        data class AddMediaFailed(val throwable: Throwable) : News()
        data class DeleteMediaFailed(val throwable: Throwable) : News()
        object ConfirmDataClear : News()
        object ExitMeasurement : News()
        data class GpsAvailabilityState(val gpsState: GpsState) : News()
    }
}
