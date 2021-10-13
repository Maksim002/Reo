package ru.ktsstudio.app_verification.domain.object_filter

import ru.ktsstudio.core_data_verfication_api.data.model.SurveyStatusType
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * @author Maxim Ovchinnikov on 19.11.2020.
 */
sealed class ObjectFilterItem {

    abstract val id: String

    data class WasteManagementTypeItem(
        val wasteManagementType: VerificationObjectType
    ) : ObjectFilterItem() {
        override val id: String
            get() = wasteManagementType.name
    }

    data class RegionItem(
        val region: Reference
    ) : ObjectFilterItem() {
        override val id: String
            get() = region.id.toString()
    }

    data class SurveyStatusItem(
        val name: String?,
        val type: SurveyStatusType
    ) : ObjectFilterItem() {
        override val id: String
            get() = type.name
    }
}
