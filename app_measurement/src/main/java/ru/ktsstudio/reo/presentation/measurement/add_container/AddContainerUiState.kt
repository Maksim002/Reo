package ru.ktsstudio.reo.presentation.measurement.add_container

/**
 * Created by Igor Park on 21/10/2020.
 */
data class AddContainerUiState(
    val isErrorVisible: Boolean,
    val isLoading: Boolean,
    val isContentVisible: Boolean,
    val isNewContainer: Boolean,
    val containerTypes: List<ContainerTypeUi>,
    val mnoContainers: List<MnoContainerUi>,
    val isOptionSelected: Boolean
)
