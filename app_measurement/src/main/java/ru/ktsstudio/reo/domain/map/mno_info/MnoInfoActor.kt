package ru.ktsstudio.reo.domain.map.mno_info

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import ru.ktsstudio.common.utils.mvi.refreshList
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.utilities.extensions.zipWithTimer

class MnoInfoActor(
    private val mnoRepository: MnoRepository,
    private val measurementRepository: MeasurementRepository,
    private val schedulers: SchedulerProvider
) : Actor<
    MnoInfoFeature.State,
    MnoInfoFeature.Wish,
    MnoInfoFeature.Effect
    > {

    override fun invoke(
        state: MnoInfoFeature.State,
        action: MnoInfoFeature.Wish
    ): Observable<MnoInfoFeature.Effect> {

        val wishToEffect = when (action) {
            is MnoInfoFeature.Wish.LoadData -> refreshList(
                createFetchAction = {
                    Single.zip(
                        mnoRepository.getMnoListByIds(action.objectIds),
                        measurementRepository.observeMeasurementsByMnoIds(action.objectIds)
                            .first(emptyList()),
                        BiFunction { mnos: List<Mno>, measurements: List<Measurement> ->
                            mnos.map { mno ->
                                mno to measurements.filter { it.mnoId == mno.objectInfo.mnoId }
                            }
                        }
                    ).zipWithTimer(FETCH_DELAY, schedulers.computation)
                },
                createLoadingAction = { MnoInfoFeature.Effect.Loading },
                createSuccessAction = (MnoInfoFeature.Effect::Success),
                createErrorAction = { MnoInfoFeature.Effect.Error(it) }
            )
        }

        return wishToEffect.observeOn(schedulers.ui)
            .toRx2Observable()
    }

    companion object {
        private const val FETCH_DELAY = 500L
    }
}
