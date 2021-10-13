package ru.ktsstudio.reo.di.create_measurement.create

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.media.MediaRepository
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.reo.domain.measurement.common.MeasurementDraftHolder
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasureReducer
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementActor
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementNewsPublisher

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal object CreateMeasurementFeatureModule {
    @Provides
    fun provideInitialState(): CreateMeasurementFeature.State {
        return CreateMeasurementFeature.State(
            measurement = null,
            fileProcessingState = emptyMap(),
            capturingMedia = null,
            isLoading = true,
            error = null,
            isEditMode = false,
            gpsEnableRejected = false
        )
    }

    @Provides
    fun provideCreateMeasurementFeature(
        initialState: CreateMeasurementFeature.State,
        mediaRepository: MediaRepository,
        locationRepository: LocationRepository,
        measurementRepository: MeasurementRepository,
        draftHolder: MeasurementDraftHolder,
        schedulers: SchedulerProvider
    ): Feature<
        CreateMeasurementFeature.Wish,
        CreateMeasurementFeature.State,
        CreateMeasurementFeature.News
        > {
        return CreateMeasurementFeature(
            initialState = initialState,
            actor = CreateMeasurementActor(
                measurementRepository,
                locationRepository,
                mediaRepository,
                schedulers,
                draftHolder
            ),
            reducer = CreateMeasureReducer(),
            newsPublisher = CreateMeasurementNewsPublisher()
        )
    }

    @Provides
    fun provideBinder(): Binder {
        return Binder()
    }
}
