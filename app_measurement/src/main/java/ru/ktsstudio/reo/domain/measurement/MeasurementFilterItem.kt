package ru.ktsstudio.reo.domain.measurement

import ru.ktsstudio.core_data_measurement_api.data.model.Mno

sealed class MeasurementFilterItem {

    abstract val id: String

    data class MnoItem(
        val mno: Mno
    ) : MeasurementFilterItem() {
        override val id: String
            get() = mno.objectInfo.mnoId
    }
}
