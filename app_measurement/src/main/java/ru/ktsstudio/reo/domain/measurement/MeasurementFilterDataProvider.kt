package ru.ktsstudio.reo.domain.measurement

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.domain.filter.data.FilterDataProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 20.10.2020.
 */
class MeasurementFilterDataProvider @Inject constructor(
    private val measurementRepository: MeasurementRepository,
    private val mnoRepository: MnoRepository
) : FilterDataProvider<MeasurementFilterItem> {
    override fun observeData(): Observable<Pair<FilterKey, List<MeasurementFilterItem>>> {
        return Observable.combineLatest(
            mnoRepository.observeMnoList(),
            observeMnoWithMeasurementIds(),
            BiFunction { mnoList, mnoWithMeasurements ->
                val items = mnoList
                    .filter { it.objectInfo.mnoId in mnoWithMeasurements }
                    .map { MeasurementFilterItem.MnoItem(it) }
                MeasurementFilterApplier.MNO_KEY to items
            }
        )
    }

    private fun observeMnoWithMeasurementIds(): Observable<Set<String>> {
        return measurementRepository.observeMeasurementList().map { it.map { it.mnoId }.toSet() }
    }
}
