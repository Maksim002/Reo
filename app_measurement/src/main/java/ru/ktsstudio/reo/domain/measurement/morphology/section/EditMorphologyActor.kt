package ru.ktsstudio.reo.domain.measurement.morphology.section

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.functions.BiFunction
import ru.ktsstudio.common.utils.mergeItems
import ru.ktsstudio.common.utils.rx.Rx2PublishSubject
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.domain.measurement.common.MorphologyDraftHolder
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyFeature.Effect
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyFeature.State
import ru.ktsstudio.reo.domain.measurement.morphology.section.EditMorphologyFeature.Wish

/**
 * Created by Igor Park on 19/10/2020.
 */
class EditMorphologyActor(
    private val measurementRepository: MeasurementRepository,
    private val schedulers: SchedulerProvider,
    private val draftHolder: MorphologyDraftHolder
) : Actor<State, Wish, Effect> {

    private val interruptSignal = Rx2PublishSubject.create<Unit>()

    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        return when (action) {
            is Wish.StartObservingMorphology -> startObservingMorphology(action.measurementId)
        }
    }

    private fun startObservingMorphology(measurementId: Long): Observable<Effect> {
        stopObservingMorphology()
        return Rx3Observable.combineLatest(
            draftHolder.observeMorphologyDraft(),
            measurementRepository.observeMorphologiesByMeasurementId(measurementId),
            BiFunction { savedDrafts: Set<MorphologyItem>, initialData: List<MorphologyItem> ->
                mergeItems(
                    higherPriorityList = savedDrafts.toList(),
                    lowerPriorityList = initialData,
                    keySelector = { it.localId }
                )
            }
        )
            .map<Effect>(Effect::DataChanged)
            .startWithItem(Effect.DataLoading)
            .onErrorReturn(Effect::DataLoadError)
            .observeOn(schedulers.ui)
            .toRx2Observable()
            .takeUntil(interruptSignal)
    }

    private fun stopObservingMorphology() = interruptSignal.onNext(Unit)
}
