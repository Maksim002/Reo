package ru.ktsstudio.reo.domain.measurement.start

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.rx.Rx3Maybe
import ru.ktsstudio.common.utils.rx.Rx3Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature.Effect
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature.State
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature.Wish

/**
 * Created by Igor Park on 10/10/2020.
 */
class StartMeasurementActor(
    private val measurementRepository: MeasurementRepository,
    private val locationRepository: LocationRepository,
    private val schedulers: SchedulerProvider
) : Actor<State, Wish, Effect> {
    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        return when (action) {
            is Wish.SetMnoActiveState -> {
                Observable.just(Effect.MnoActiveStateChange(action.isActive))
            }
            is Wish.ResolveMeasurement -> {
                if (state.isMnoActive) {
                    Observable.just(Effect.MeasurementResolvedToProceed(action.mnoId))
                } else {
                    Observable.just(Effect.MeasurementResolvedToSkipp)
                }
            }
            is Wish.SkipMeasurement -> {
                sendMeasurementWithComment(
                    action.mnoId,
                    action.impossibilityReason,
                    action.withLocation
                )
            }
            is Wish.AddComment -> {
                Observable.just(Effect.CommentAdded(action.comment))
            }
        }
    }

    private fun sendMeasurementWithComment(
        mnoId: String,
        impossibilityReason: String,
        withLocation: Boolean
    ): Observable<Effect> {
        val locationMaybe = if (withLocation) {
            locationRepository.getCurrentLocation()
                .onErrorReturnItem(GpsPoint.EMPTY)
        } else {
            Rx3Maybe.just(GpsPoint.EMPTY)
        }
        return locationMaybe
            .defaultIfEmpty(GpsPoint.EMPTY)
            .flatMapCompletable { gps ->
                measurementRepository.setMeasurementImpossible(
                    mnoId = mnoId,
                    impossibilityReason = impossibilityReason,
                    gpsPoint = gps.takeIf { it.isEmpty().not() },
                    date = Instant.now()
                )
            }
            .andThen(Rx3Observable.just<Effect>(Effect.MeasurementSkipped))
            .startWithItem(Effect.MeasurementUpdating)
            .onErrorReturn(Effect::MeasurementFailed)
            .observeOn(schedulers.ui)
            .toRx2Observable()
    }
}
