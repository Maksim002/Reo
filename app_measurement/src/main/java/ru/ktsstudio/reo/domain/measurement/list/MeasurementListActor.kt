package ru.ktsstudio.reo.domain.measurement.list

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.common.domain.filter.doIfFilterChange
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.reo.domain.measurement.MeasurementFilterApplier

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
internal class MeasurementListActor(
    private val measurementRepository: MeasurementRepository,
    private val mnoRepository: MnoRepository,
    private val schedulers: SchedulerProvider,
    private val measurementFilterApplier: MeasurementFilterApplier,
    private val filterUpdater: FilterUpdater
) : Actor<MeasurementListFeature.State, MeasurementListFeature.Wish, MeasurementListFeature.Effect> {

    private val interruptSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: MeasurementListFeature.State,
        action: MeasurementListFeature.Wish
    ): Observable<out MeasurementListFeature.Effect> {
        return when (action) {
            is MeasurementListFeature.Wish.Load -> observeMeasurementList(
                state.currentFilter,
                state.sort
            )
            is MeasurementListFeature.Wish.ChangeFilter -> {
                changeFilter(action.newFilter, state)
            }
            is MeasurementListFeature.Wish.ChangeSort -> {
                observeMeasurementList(state.currentFilter, action.newSort)
                    .startWith(MeasurementListFeature.Effect.UpdatingSort(action.newSort))
            }
            is MeasurementListFeature.Wish.ChangeSearchQuery -> {
                val newFilter = state.currentFilter.copy(searchQuery = action.searchQuery)
                changeFilter(newFilter, state)
            }
        }
    }

    private fun changeFilter(
        newFilter: Filter,
        state: MeasurementListFeature.State
    ): Observable<MeasurementListFeature.Effect> {
        return doIfFilterChange(
            newFilter,
            state.currentFilter,
            MeasurementListFeature.Effect.UpdatingFilter(newFilter)
        ) {
            filterUpdater.updateFilter(newFilter)
            observeMeasurementList(newFilter, state.sort)
        }
    }

    private fun observeMeasurementList(
        newFilter: Filter,
        sort: MeasurementSort
    ): Observable<MeasurementListFeature.Effect> {
        fun pairWithMno(measurements: List<Measurement>): Rx3Observable<List<Pair<Measurement, Mno>>> {
            val mnoIds = measurements.map { it.mnoId }
            return mnoRepository.observeMnoListByIds(mnoIds)
                .map { mnoList ->
                    measurements.map { measurement ->
                        measurement to mnoList.first { measurement.mnoId == it.objectInfo.mnoId }
                    }
                }
        }

        interruptSignal.onNext(Unit)
        return measurementRepository.observeMeasurementList()
            .switchMap(::pairWithMno)
            .map<MeasurementListFeature.Effect> {
                MeasurementListFeature.Effect.Success(
                    measurementWithMnoList = it.sortMeasurements(sort)
                        .filter { (measurement, mno) ->
                            measurementFilterApplier.applyFilter(measurement, mno, newFilter)
                        }
                )
            }
            .startWithItem(MeasurementListFeature.Effect.Loading)
            .onErrorReturn {
                MeasurementListFeature.Effect.Error(it)
            }
            .observeOn(schedulers.ui)
            .takeUntil(interruptSignal)
            .toRx2Observable()
    }

    private fun List<Pair<Measurement, Mno>>.sortMeasurements(sort: MeasurementSort): List<Pair<Measurement, Mno>> {
        return when (sort) {
            MeasurementSort.CREATED_AT -> sortedByDescending { it.first.date }
            MeasurementSort.STATUS -> sortedWith(
                compareBy<Pair<Measurement, Mno>> { it.first.status.order }
                    .thenByDescending { it.first.date }
            )
        }
    }
}
