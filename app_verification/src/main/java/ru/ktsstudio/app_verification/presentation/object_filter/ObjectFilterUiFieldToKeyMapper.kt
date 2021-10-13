package ru.ktsstudio.app_verification.presentation.object_filter

import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.presentation.filter.FilterUiFieldToKeyMapper
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectFilterApplier

/**
 * @author Maxim Ovchinnikov on 19.11.2020.
 */
class ObjectFilterUiFieldToKeyMapper : FilterUiFieldToKeyMapper<ObjectFilterField> {

    override fun map(from: ObjectFilterField): FilterKey {
        return when (from) {
            ObjectFilterField.WASTE_MANAGEMENT_TYPE -> VerificationObjectFilterApplier.WASTE_MANAGEMENT_TYPE_KEY
            ObjectFilterField.REGION -> VerificationObjectFilterApplier.REGION_KEY
            ObjectFilterField.SURVEY_STATUS -> VerificationObjectFilterApplier.SURVEY_STATUS_KEY
        }
    }
}
