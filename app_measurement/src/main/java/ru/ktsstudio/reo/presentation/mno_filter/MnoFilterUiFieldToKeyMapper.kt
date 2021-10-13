package ru.ktsstudio.reo.presentation.mno_filter

import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.presentation.filter.FilterUiFieldToKeyMapper
import ru.ktsstudio.core_data_measurement_api.data.model.MnoFilterApplier

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class MnoFilterUiFieldToKeyMapper : FilterUiFieldToKeyMapper<MnoFilterField> {
    override fun map(from: MnoFilterField): FilterKey {
        return when (from) {
            MnoFilterField.CATEGORY -> MnoFilterApplier.CATEGORY_KEY
        }
    }
}
