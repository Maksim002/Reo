package ru.ktsstudio.reo.domain.measurement.edit_mixed_container

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.MixedWasteContainerDraft
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm

/**
 * Created by Igor Park on 19/10/2020.
 */
class EditMixedContainerFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    EditMixedContainerFeature.Wish,
    EditMixedContainerFeature.Effect,
    EditMixedContainerFeature.State,
    EditMixedContainerFeature.News
    >(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher,
        bootstrapper = null
    ) {
    data class State(
        val containerDraft: MixedWasteContainerDraft,
        val requiredDataTypes: Set<ContainerDataType>,
        val isLoading: Boolean,
        val isUpdating: Boolean,
        val error: Throwable?
    )

    sealed class Wish {
        data class InitData(
            val containerId: Long?,
            val mnoContainerId: String?,
            val containerTypeId: String?
        ) : Wish()

        data class UpdateField(
            val dataType: ContainerDataType,
            val value: String
        ) : Wish()

        data class SaveData(val measurementId: Long, val containerId: Long?) : Wish()
        data class DeleteData(val containerId: Long) : Wish()
    }

    sealed class Effect {
        object DataLoading : Effect()
        data class DataLoadError(override val throwable: Throwable) : Effect(), ErrorEffect
        data class DataInitialized(val containerDraft: MixedWasteContainerDraft) : Effect()
        object DataUpdating : Effect()
        object DataUpdateCompleted : Effect()
        data class DataUpdateFailed(override val throwable: Throwable) : Effect(), ErrorEffect
        data class FormChanged(val form: MeasurementForm) : Effect()
    }

    sealed class News {
        data class DataUpdateFailed(val throwable: Throwable) : News()
        object DataUpdateCompleted : News()
        data class FormChanged(
            val form: MeasurementForm
        ) : News()
    }
}
