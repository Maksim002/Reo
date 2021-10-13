package ru.ktsstudio.reo.domain.measurement.details

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */

internal class MeasurementDetailsFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    MeasurementDetailsFeature.Wish,
    MeasurementDetailsFeature.Effect,
    MeasurementDetailsFeature.State,
    MeasurementDetailsFeature.News>(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher,
        bootstrapper = null
    ) {

    data class State(
        val data: Measurement? = null,
        val isEditable: Boolean = false,
        val isPreviewMode: Boolean = false,
        val loading: Boolean = true,
        val isCreating: Boolean = false,
        val error: Throwable? = null
    )

    sealed class Wish {
        data class Load(val measurementId: Long, val isPreviewMode: Boolean) : Wish()
        object CreateMeasurement : Wish()
        object EditMeasurement : Wish()
    }

    sealed class Effect {
        object Loading : Effect()
        data class PreviewModeInfo(val isPreview: Boolean) : Effect()
        object MeasurementCreated : Effect()
        object MeasurementCreateInProcess : Effect()
        data class MeasurementCreateFailed(
            override val throwable: Throwable
        ) : Effect(), ErrorEffect

        data class OpenEditMeasurement(
            val mnoId: String,
            val measurementId: Long
        ) : Effect()

        data class Success(
            val measurementDetails: Measurement,
            val isEditable: Boolean
        ) : Effect()

        data class Error(
            override val throwable: Throwable
        ) : Effect(), ErrorEffect
    }

    sealed class News {
        data class MeasurementCreateFailed(val throwable: Throwable) : News()
        object MeasurementCreated : News()
        data class OpenEditMeasurement(
            val mnoId: String,
            val measurementId: Long
        ) : News()
    }
}
