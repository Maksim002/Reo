package ru.ktsstudio.reo.presentation.measurement.create_measurement

import ru.ktsstudio.core_data_measurement_api.domain.MeasurementComposite
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory

sealed class MeasurementMediaUi {
    data class Media(
        val media: MeasurementComposite.Media,
        val isLoading: Boolean
    ) : MeasurementMediaUi()

    data class AddItem(val category: MeasurementMediaCategory) : MeasurementMediaUi()
}
