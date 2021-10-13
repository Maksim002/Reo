package ru.ktsstudio.app_verification.presentation.object_filter

import ru.ktsstudio.app_verification.domain.object_filter.ObjectFilterItem
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterFeature
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.domain.filter.data.FilterDataFeature
import ru.ktsstudio.common.presentation.filter.FilterDataUiState
import ru.ktsstudio.common.presentation.filter.FilterUiFieldToKeyMapper
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.utilities.extensions.orFalse
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 19.11.2020.
 */
class ObjectFilterUiStateTransformer @Inject constructor(
    private val filterFieldToKeyMapper: FilterUiFieldToKeyMapper<ObjectFilterField>,
    private val verificationObjectTypeToStringMapper: Mapper<VerificationObjectType, String>
) : (Pair<FilterFeature.State, FilterDataFeature.State<ObjectFilterItem>>)
-> FilterDataUiState<ObjectFilterField, ObjectFilterUiItem> {

    override fun invoke(
        state: Pair<FilterFeature.State, FilterDataFeature.State<ObjectFilterItem>>
    ): FilterDataUiState<ObjectFilterField, ObjectFilterUiItem> {
        val filter = state.first.currentFilter
        val dataMap = state.second.data
        return mapOf(
            ObjectFilterField.WASTE_MANAGEMENT_TYPE to createUiWasteManagementTypeItems(
                selectedItemId = getFilterItemId(ObjectFilterField.WASTE_MANAGEMENT_TYPE, filter),
                wasteManagementTypeItems = getDataItems(ObjectFilterField.WASTE_MANAGEMENT_TYPE, dataMap)
            ),
            ObjectFilterField.REGION to createUiRegionItems(
                selectedItemId = getFilterItemId(ObjectFilterField.REGION, filter),
                regions = getDataItems(ObjectFilterField.REGION, dataMap)
            ),
            ObjectFilterField.SURVEY_STATUS to createSurveyStatusItems(
                selectedItemId = getFilterItemId(ObjectFilterField.SURVEY_STATUS, filter),
                statuses = getDataItems(ObjectFilterField.SURVEY_STATUS, dataMap)
            )
        )
    }

    private fun getFilterItemId(field: ObjectFilterField, filter: Filter): String? {
        val filterKey = filterFieldToKeyMapper.map(field)
        return filter.filterMap[filterKey] as? String
    }

    private fun getDataItems(
        field: ObjectFilterField,
        dataMap: Map<FilterKey, List<ObjectFilterItem>>
    ): List<ObjectFilterItem> {
        return dataMap[filterFieldToKeyMapper.map(field)].orEmpty()
    }

    private fun createUiWasteManagementTypeItems(
        selectedItemId: String?,
        wasteManagementTypeItems: List<ObjectFilterItem>
    ): List<ObjectFilterUiItem.WasteManagementType> {
        return wasteManagementTypeItems.filterIsInstance<ObjectFilterItem.WasteManagementTypeItem>()
            .map { wasteManagementTypeItem ->
                ObjectFilterUiItem.WasteManagementType(
                    id = wasteManagementTypeItem.id,
                    title = wasteManagementTypeItem.wasteManagementType.let(verificationObjectTypeToStringMapper::map),
                    isSelected = selectedItemId?.let { it == wasteManagementTypeItem.id }.orFalse()
                )
            }
    }

    private fun createUiRegionItems(
        selectedItemId: String?,
        regions: List<ObjectFilterItem>
    ): List<ObjectFilterUiItem.Region> {
        return regions.filterIsInstance<ObjectFilterItem.RegionItem>()
            .map { region ->
                ObjectFilterUiItem.Region(
                    id = region.id,
                    title = region.region.name,
                    isSelected = selectedItemId?.let { it == region.id }.orFalse()
                )
            }
    }

    private fun createSurveyStatusItems(
        selectedItemId: String?,
        statuses: List<ObjectFilterItem>
    ): List<ObjectFilterUiItem.SurveyStatus> {
        return statuses.filterIsInstance<ObjectFilterItem.SurveyStatusItem>()
            .map { status ->
                ObjectFilterUiItem.SurveyStatus(
                    id = status.id,
                    title = status.name ?: status.type.name,
                    isSelected = selectedItemId?.let { it == status.id }.orFalse()
                )
            }
    }
}
