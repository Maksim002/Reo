package ru.ktsstudio.reo.domain.measurement.add_container

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import io.reactivex.rxjava3.functions.BiFunction
import ru.ktsstudio.common.utils.rx.Rx3Single
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerFeature.Effect
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerFeature.State
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerFeature.Wish

/**
 * Created by Igor Park on 19/10/2020.
 */
class AddContainerActor(
    private val measurementRepository: MeasurementRepository,
    private val mnoRepository: MnoRepository,
    private val schedulers: SchedulerProvider
) : Actor<State, Wish, Effect> {
    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        return when (action) {
            is Wish.InitData -> {
                Rx3Single.zip(
                    mnoRepository.getMnoContainersByMnoId(action.mnoId),
                    measurementRepository.getContainerTypes(),
                    BiFunction { mnoContainers: List<MnoContainer>, types: List<ContainerType> ->
                        mnoContainers to types
                    }
                )
                    .toObservable()
                    .map<Effect> { (mnoContainers, types) ->
                        Effect.DataInitialized(
                            mnoContainers = mnoContainers,
                            containerTypes = types
                        )
                    }
                    .startWithItem(Effect.DataLoading)
                    .onErrorReturn(Effect::DataLoadingError)
                    .observeOn(schedulers.ui)
                    .toRx2Observable()
            }

            is Wish.SelectNewContainer -> {
                Observable.just(
                    Effect.OptionSelected(
                        isNewContainer = true,
                        selectedMnoContainerId = null,
                        selectedContainerTypeId = null
                    )
                )
            }

            is Wish.SelectMnoContainer -> {
                Observable.just(
                    Effect.OptionSelected(
                        isNewContainer = false,
                        selectedMnoContainerId = action.selectedMnoContainerId,
                        selectedContainerTypeId = null
                    )
                )
            }

            is Wish.SelectContainerType -> {
                Observable.just(
                    Effect.OptionSelected(
                        isNewContainer = true,
                        selectedContainerTypeId = action.selectedContainerTypeId,
                        selectedMnoContainerId = null
                    )
                )
            }
        }
    }
}
