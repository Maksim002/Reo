package ru.ktsstudio.core_data_verfication_api.data.model

import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.utils.ContainsTextChecker
import ru.ktsstudio.common.utils.mapper.Mapper
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 18.11.2020.
 */
class VerificationObjectFilterApplier @Inject constructor(
    private val containsTextChecker: ContainsTextChecker,
    private val verificationObjectTypeToStringMapper: Mapper<VerificationObjectType, String>
) {

    fun applyFilter(verificationObject: VerificationObject, filter: Filter): Boolean = with(verificationObject) {

        fun checkSearch(): Boolean {
            val fieldsToSearch = listOfNotNull(
                generalInformation.name,
                generalInformation.addressDescription.orEmpty(),
                verificationObjectTypeToStringMapper.map(type)
            )
            return filter.searchQuery.isBlank() ||
                fieldsToSearch.any { containsTextChecker.contains(filter.searchQuery, it) }
        }

        fun checkWasteManagementType(): Boolean {
            val wasteManagementType = filter.filterMap[WASTE_MANAGEMENT_TYPE_KEY]
                as? String
                ?: return true
            return this.type.name == wasteManagementType
        }

        fun checkStatus(): Boolean {
            val statusId = filter.filterMap[STATUS_KEY]?.toString() ?: return true
            return objectStatus?.id == statusId
        }

        fun checkRegion(): Boolean {
            val regionId = filter.filterMap[REGION_KEY]?.toString() ?: return true
            return this.generalInformation.subject.id == regionId
        }

        fun checkSurveyStatus(): Boolean {
            val statusId = filter.filterMap[SURVEY_STATUS_KEY]?.toString() ?: return true
            return status?.type?.name == statusId
        }

        checkSearch() &&
            checkWasteManagementType() &&
            checkStatus() &&
            checkRegion() &&
            checkSurveyStatus()
    }

    companion object {
        const val STATUS_KEY: FilterKey = "status"
        const val WASTE_MANAGEMENT_TYPE_KEY: FilterKey = "waste_management_type"
        const val REGION_KEY: FilterKey = "region"
        const val SURVEY_STATUS_KEY: FilterKey = "survey_status_key"
    }
}