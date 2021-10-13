package ru.ktsstudio.reo.di.create_measurement.start

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.reo.domain.measurement.start.StartMeasureNewsPublisher
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementActor
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementFeature
import ru.ktsstudio.reo.domain.measurement.start.StartMeasurementReducer

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal object StartMeasurementFeatureModule {
    @Provides
    @JvmStatic
    fun provideInitialState(): StartMeasurementFeature.State {
        return StartMeasurementFeature.State(
            isUpdating = false,
            isMnoActive = false,
            isOptionSelected = false,
            comment = ""
        )
    }

    @Provides
    @JvmStatic
    fun provideStartMeasurementFeature(
        initialState: StartMeasurementFeature.State,
        locationRepository: LocationRepository,
        measurementRepository: MeasurementRepository,
        schedulers: SchedulerProvider
    ): Feature<
        StartMeasurementFeature.Wish,
        StartMeasurementFeature.State,
        StartMeasurementFeature.News
        > {
        return StartMeasurementFeature(
            initialState = initialState,
            actor = StartMeasurementActor(
                measurementRepository,
                locationRepository,
                schedulers
            ),
            reducer = StartMeasurementReducer(),
            newsPublisher = StartMeasureNewsPublisher()
        )
    }

    @Provides
    @JvmStatic
    fun provideBinder(): Binder {
        return Binder()
    }
}
