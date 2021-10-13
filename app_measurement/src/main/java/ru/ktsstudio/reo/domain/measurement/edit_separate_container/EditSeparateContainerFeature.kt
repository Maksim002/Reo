package ru.ktsstudio.reo.domain.measurement.edit_separate_container

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType

/**
 * Created by Igor Park on 19/10/2020.
 */
class EditSeparateContainerFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>
) : BaseMviFeature<
    EditSeparateContainerFeature.Wish,
    EditSeparateContainerFeature.Effect,
    EditSeparateContainerFeature.State,
    EditSeparateContainerFeature.News
    >(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        newsPublisher = newsPublisher,
        bootstrapper = null
    ) {
    data class State(
        val separateContainer: SeparateContainerComposite,
        val requiredDataTypes: Set<ContainerDataType>,
        val isLoading: Boolean,
        val error: Throwable?
    )

    sealed class Wish {
        data class InitData(
            val measurementId: Long,
            val containerId: Long?,
            val mnoContainerId: String?,
            val containerTypeId: String?
        ) : Wish()

        data class UpdateField(
            val dataType: ContainerDataType,
            val value: String
        ) : Wish()

        object SaveData : Wish()
        object DeleteData : Wish()
        object ClearUnfinishedData : Wish()
    }

    sealed class Effect {
        data class DataInitialized(val container: SeparateContainerComposite) : Effect()
        object DataLoading : Effect()
        data class DataLoadError(override val throwable: Throwable) : Effect(), ErrorEffect

        object DataUpdated : Effect()
        data class DataUpdateFailed(override val throwable: Throwable) : Effect(), ErrorEffect

        data class WasteTypesUpdated(val wasteTypes: List<ContainerWasteType>) : Effect()
        object DataCleared : Effect()
    }

    sealed class News {
        data class DataUpdateFailed(val throwable: Throwable) : News()
        object DataUpdated : News()
        object DataCleared : News()
    }
}
