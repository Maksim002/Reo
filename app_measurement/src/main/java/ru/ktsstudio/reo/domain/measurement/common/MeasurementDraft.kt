package ru.ktsstudio.reo.domain.measurement.common

import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite

data class MeasurementDraft(
    val separateContainers: Set<SeparateContainerComposite>,
    val mixedContainers: Set<MixedWasteContainerComposite>,
    val morphologyItems: Set<MorphologyItem>,
    val comment: String
) {
    companion object {
        val EMPTY = MeasurementDraft(
            emptySet(),
            emptySet(),
            emptySet(),
            ""
        )
    }
}
