package ru.ktsstudio.reo.domain.measurement

import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.utils.ContainsTextChecker
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 20.10.2020.
 */
class MeasurementFilterApplier @Inject constructor(
    private val containsTextChecker: ContainsTextChecker
) {

    fun applyFilter(measurement: Measurement, mno: Mno, filter: Filter): Boolean = with(mno) {
        fun checkSearch(): Boolean {
            val fieldsToSearch = listOfNotNull(
                sourceAddress.municipalDistrict,
                sourceAddress.federalDistrict,
                sourceAddress.region,
                source.name
            )
            return filter.searchQuery.isBlank() ||
                fieldsToSearch.any { containsTextChecker.contains(filter.searchQuery, it) }
        }

        fun checkMno(): Boolean {
            val mnoId = filter.filterMap[MNO_KEY]?.toString() ?: return true
            return mno.objectInfo.mnoId == mnoId
        }

        checkSearch() && checkMno()
    }

    companion object {
        const val MNO_KEY: FilterKey = "mno"
    }
}
