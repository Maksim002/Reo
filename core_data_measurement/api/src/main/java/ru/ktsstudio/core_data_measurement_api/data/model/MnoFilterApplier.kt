package ru.ktsstudio.core_data_measurement_api.data.model

import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.utils.ContainsTextChecker
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
//TODO move to common with models
class MnoFilterApplier @Inject constructor(
    private val containsTextChecker: ContainsTextChecker
) {

    fun applyFilter(mno: Mno, filter: Filter): Boolean = with(mno) {
        fun checkSearch(): Boolean {
            val fieldsToSearch = listOfNotNull(
                sourceAddress.federalDistrict,
                sourceAddress.region,
                sourceAddress.municipalDistrict,
                sourceAddress.address,
                source.name,
                source.category.name
            )
            return filter.searchQuery.isBlank() ||
                fieldsToSearch.any { containsTextChecker.contains(filter.searchQuery, it) }
        }

        fun checkCategory(): Boolean {
            val category = filter.filterMap[CATEGORY_KEY]?.toString() ?: return true
            return mno.source.category.id == category
        }

        checkSearch() && checkCategory()
    }

    companion object {
        const val CATEGORY_KEY: FilterKey = "category"
    }
}