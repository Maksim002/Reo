package ru.ktsstudio.reo.presentation.measurement_filter

import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.presentation.filter.FilterUiFieldToKeyMapper
import ru.ktsstudio.reo.domain.measurement.MeasurementFilterApplier

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class MeasurementFilterUiFieldToKeyMapper : FilterUiFieldToKeyMapper<MeasurementFilterField> {
    override fun map(from: MeasurementFilterField): FilterKey {
        return when (from) {
            MeasurementFilterField.MNO -> MeasurementFilterApplier.MNO_KEY
        }
    }
}
