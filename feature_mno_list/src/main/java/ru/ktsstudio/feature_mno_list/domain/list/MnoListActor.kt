package ru.ktsstudio.feature_mno_list.domain.list

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.functions.BiFunction
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
import ru.ktsstudio.core_data_measurement_api.data.model.MnoFilterApplier

/**
 * @author Maxim Ovchinnikov on 01.10.2020.
 */
internal class MnoListActor(
    private val mnoRepository: MnoRepository,
    private val measurementsRepository: MeasurementRepository,
    private val schedulers: SchedulerProvider,
    private val filterUpdater: FilterUpdater,
    private val mnoFilterApplier: MnoFilterApplier
) : Actor<MnoListFeature.State, MnoListFeature.Wish, MnoListFeature.Effect> {

    private val interruptSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: MnoListFeature.State,
        action: MnoListFeature.Wish
    ): Observable<out MnoListFeature.Effect> {
        return when (action) {
            is MnoListFeature.Wish.Load -> observeMnoList(state.currentFilter)
            is MnoListFeature.Wish.ChangeSearchQuery -> {
                val newFilter = state.currentFilter.copy(searchQuery = action.searchQuery)
                doIfFilterChange(
                    newFilter,
                    state.currentFilter,
                    MnoListFeature.Effect.UpdatingFilter(newFilter)
                ) {
                    filterUpdater.updateFilter(newFilter)
                    observeMnoList(newFilter)
                }
            }
            is MnoListFeature.Wish.ChangeFilter -> {
                doIfFilterChange(
                    action.newFilter,
                    state.currentFilter,
                    MnoListFeature.Effect.UpdatingFilter(action.newFilter)
                ) {
                    observeMnoList(action.newFilter)
                }
            }
        }
    }

    private fun observeMnoList(filter: Filter): Observable<MnoListFeature.Effect> {
        interruptSignal.onNext(Unit)

        fun observeMnoInternal(): Rx3Observable<List<Mno>> {
            return mnoRepository.observeMnoList()
                .map { it.filter { mnoFilterApplier.applyFilter(it, filter) } }
        }

        return Rx3Observable.combineLatest<List<Mno>, List<Measurement>, MnoListFeature.Effect>(
            observeMnoInternal(),
            measurementsRepository.observeMeasurementList(),
            BiFunction { mnoList, measurements ->
                MnoListFeature.Effect.Success(
                    mnoList = mnoList,
                    mnoIdToMeasurementsMap = measurements.groupBy { it.mnoId }
                )
            }
        )
            .startWithItem(MnoListFeature.Effect.Loading)
            .onErrorReturn { MnoListFeature.Effect.Error(it) }
            .takeUntil(interruptSignal)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }
}