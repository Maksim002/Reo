package ru.ktsstudio.reo.domain.measurement.add_container

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import ru.ktsstudio.common.utils.mvi.BaseMviFeature
import ru.ktsstudio.common.utils.mvi.ErrorEffect
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer

/**
 * Created by Igor Park on 19/10/2020.
 */
class AddContainerFeature(
    initialState: State,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>
) : BaseMviFeature<
    AddContainerFeature.Wish,
    AddContainerFeature.Effect,
    AddContainerFeature.State,
    Nothing
    >(
        initialState = initialState,
        actor = actor,
        reducer = reducer,
        bootstrapper = null,
        newsPublisher = null
    ) {
    data class State(
        val isNewContainer: Boolean,
        val containerTypes: List<ContainerType>,
        val mnoContainers: List<MnoContainer>,
        val selectedMnoContainerId: String?,
        val selectedContainerTypeId: String?,
        val isLoading: Boolean,
        val error: Throwable?
    )

    sealed class Wish {
        data class InitData(val mnoId: String) : Wish()
        object SelectNewContainer : Wish()
        data class SelectMnoContainer(val selectedMnoContainerId: String) : Wish()
        data class SelectContainerType(val selectedContainerTypeId: String) : Wish()
    }

    sealed class Effect {
        object DataLoading : Effect()
        data class DataLoadingError(override val throwable: Throwable) : Effect(), ErrorEffect
        data class DataInitialized(
            val containerTypes: List<ContainerType>,
            val mnoContainers: List<MnoContainer>
        ) : Effect()

        data class OptionSelected(
            val isNewContainer: Boolean,
            val selectedMnoContainerId: String?,
            val selectedContainerTypeId: String?
        ) : Effect()
    }
}
