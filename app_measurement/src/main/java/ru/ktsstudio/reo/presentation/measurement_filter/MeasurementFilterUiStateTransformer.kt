package ru.ktsstudio.reo.presentation.measurement_filter

import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterFeature
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.domain.filter.data.FilterDataFeature
import ru.ktsstudio.common.presentation.filter.FilterDataUiState
import ru.ktsstudio.common.presentation.filter.FilterUiFieldToKeyMapper
import ru.ktsstudio.reo.domain.measurement.MeasurementFilterItem
import ru.ktsstudio.utilities.extensions.orFalse
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class MeasurementFilterUiStateTransformer @Inject constructor(
    private val filterFieldToKeyMapper: FilterUiFieldToKeyMapper<MeasurementFilterField>
) : (Pair<FilterFeature.State, FilterDataFeature.State<MeasurementFilterItem>>)
    -> FilterDataUiState<MeasurementFilterField, MeasurementFilterUiItem> {

    override fun invoke(
        state: Pair<FilterFeature.State, FilterDataFeature.State<MeasurementFilterItem>>
    ): FilterDataUiState<MeasurementFilterField, MeasurementFilterUiItem> {
        val filter = state.first.currentFilter
        val dataMap = state.second.data
        return mapOf(
            MeasurementFilterField.MNO to createUiCategoryItems(
                getFilterItemId(MeasurementFilterField.MNO, filter),
                getDataItems(MeasurementFilterField.MNO, dataMap)
            )
        )
    }

    private fun getFilterItemId(field: MeasurementFilterField, filter: Filter): String? {
        val filterKey = filterFieldToKeyMapper.map(field)
        return filter.filterMap[filterKey] as? String
    }

    private fun getDataItems(
        field: MeasurementFilterField,
        dataMap: Map<FilterKey, List<MeasurementFilterItem>>
    ): List<MeasurementFilterItem> {
        return dataMap[filterFieldToKeyMapper.map(field)].orEmpty()
    }

    private fun createUiCategoryItems(
        selectedItemId: String?,
        mnoItems: List<MeasurementFilterItem>
    ): List<MeasurementFilterUiItem.Mno> {
        return mnoItems.mapNotNull {
            it as? MeasurementFilterItem.MnoItem
        }.map { mnoItem ->
            val mno = mnoItem.mno
            MeasurementFilterUiItem.Mno(
                id = mno.objectInfo.mnoId,
                title = mno.source.name,
                category = mno.source.category.name,
                address = mno.sourceAddress.address,
                isSelected = selectedItemId?.let { it == mno.objectInfo.mnoId }.orFalse()
            )
        }
    }
}
