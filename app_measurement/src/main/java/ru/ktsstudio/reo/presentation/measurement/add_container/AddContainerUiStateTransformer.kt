package ru.ktsstudio.reo.presentation.measurement.add_container

import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerFeature

/**
 * Created by Igor Park on 21/10/2020.
 */
class AddContainerUiStateTransformer : (AddContainerFeature.State) -> AddContainerUiState {
    override fun invoke(state: AddContainerFeature.State): AddContainerUiState = with(state) {
        return AddContainerUiState(
            isErrorVisible = error != null && isLoading.not(),
            isLoading = isLoading,
            isContentVisible = error == null && isLoading.not(),
            containerTypes = containerTypes.map { it.mapTypeToUi(selectedContainerTypeId) },
            mnoContainers = mnoContainers.map { it.mapContainerToUi(selectedMnoContainerId) },
            isNewContainer = isNewContainer,
            isOptionSelected = selectedContainerTypeId != null ||
                selectedMnoContainerId != null
        )
    }

    private fun ContainerType.mapTypeToUi(selectedId: String?): ContainerTypeUi {
        return ContainerTypeUi(
            containerType = this,
            isSelected = id == selectedId
        )
    }

    private fun MnoContainer.mapContainerToUi(selectedId: String?): MnoContainerUi {
        return MnoContainerUi(
            mnoContainer = this,
            isSelected = id == selectedId
        )
    }
}
