package ru.ktsstudio.reo.presentation.mno_filter

import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterFeature
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.domain.filter.data.FilterDataFeature
import ru.ktsstudio.common.presentation.filter.FilterDataUiState
import ru.ktsstudio.common.presentation.filter.FilterUiFieldToKeyMapper
import ru.ktsstudio.reo.domain.mno_filter.MnoFilterItem
import ru.ktsstudio.utilities.extensions.orFalse
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class MnoFilterUiStateTransformer @Inject constructor(
    private val filterFieldToKeyMapper: FilterUiFieldToKeyMapper<MnoFilterField>
) : (Pair<FilterFeature.State, FilterDataFeature.State<MnoFilterItem>>)
    -> FilterDataUiState<MnoFilterField, MnoFilterUiItem> {

    override fun invoke(
        state: Pair<FilterFeature.State, FilterDataFeature.State<MnoFilterItem>>
    ): FilterDataUiState<MnoFilterField, MnoFilterUiItem> {
        val filter = state.first.currentFilter
        val dataMap = state.second.data
        return mapOf(
            MnoFilterField.CATEGORY to createUiCategoryItems(
                getFilterItemId(MnoFilterField.CATEGORY, filter),
                getDataItems(MnoFilterField.CATEGORY, dataMap)
            )
        )
    }

    private fun getFilterItemId(field: MnoFilterField, filter: Filter): String? {
        val filterKey = filterFieldToKeyMapper.map(field)
        return filter.filterMap[filterKey] as? String
    }

    private fun getDataItems(
        field: MnoFilterField,
        dataMap: Map<FilterKey, List<MnoFilterItem>>
    ): List<MnoFilterItem> {
        return dataMap[filterFieldToKeyMapper.map(field)].orEmpty()
    }

    private fun createUiCategoryItems(
        selectedItemId: String?,
        categoryItems: List<MnoFilterItem>
    ): List<MnoFilterUiItem.MnoCategory> {
        return categoryItems.mapNotNull {
            it as? MnoFilterItem.CategoryItem
        }.map { mnoCategory ->
            val category = mnoCategory.category
            MnoFilterUiItem.MnoCategory(
                id = category.id,
                title = category.name,
                isSelected = selectedItemId?.let { it == category.id }.orFalse()
            )
        }
    }
}
