package ru.ktsstudio.feature_mno_list.domain.details

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.utilities.extensions.zipWithTimer
import java.util.concurrent.TimeUnit

/**
 * @author Maxim Myalkin (MaxMyalkin) on 03.10.2020.
 */
internal class MnoDetailsActor(
    private val repository: MnoRepository,
    private val measurementRepository: MeasurementRepository,
    private val schedulerProvider: SchedulerProvider,
) : Actor<MnoDetailsFeature.State, MnoDetailsFeature.Wish, MnoDetailsFeature.Effect> {

    private val interruptSignal = PublishSubject.create<Unit>()

    override fun invoke(
        state: MnoDetailsFeature.State,
        action: MnoDetailsFeature.Wish
    ): Observable<out MnoDetailsFeature.Effect> {
        return when (action) {
            is MnoDetailsFeature.Wish.Load -> observeDetails(action.id)
        }
    }

    private fun observeDetails(id: String): Observable<MnoDetailsFeature.Effect> {
        interruptSignal.onNext(Unit)

        return Rx3Observable.combineLatest<Mno, List<Measurement>, Long, MnoDetailsFeature.Effect>(
            repository.observeMnoById(id),
            measurementRepository.observeMeasurementList(),
            Rx3Observable.timer(DELAY, TimeUnit.MILLISECONDS, schedulerProvider.computation),
            Function3 { mno, measurements, _ ->
                val mnoMeasurements = measurements
                    .filter { it.mnoId == mno.objectInfo.mnoId }
                MnoDetailsFeature.Effect.Success(mno, mnoMeasurements)
            }
        )
            .startWithItem(MnoDetailsFeature.Effect.Loading)
            .onErrorReturn { MnoDetailsFeature.Effect.Error(it) }
            .observeOn(schedulerProvider.ui)
            .takeUntil(interruptSignal)
            .toRx2Observable()
    }

    companion object {
        private const val DELAY = 500L
    }
}