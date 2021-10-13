package ru.ktsstudio.reo.di.create_measurement.add_container

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerActor
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerFeature
import ru.ktsstudio.reo.domain.measurement.add_container.AddContainerReducer

/**
 * Created by Igor Park on 25/09/2020.
 */
@Module
internal object AddContainerFeatureModule {
    @Provides
    fun provideInitialState(): AddContainerFeature.State {
        return AddContainerFeature.State(
            isNewContainer = false,
            containerTypes = emptyList(),
            mnoContainers = emptyList(),
            selectedContainerTypeId = null,
            selectedMnoContainerId = null,
            isLoading = true,
            error = null
        )
    }

    @Provides
    fun provideCreateMeasurementFeature(
        initialState: AddContainerFeature.State,
        measurementRepository: MeasurementRepository,
        mnoRepository: MnoRepository,
        schedulers: SchedulerProvider
    ): Feature<
        AddContainerFeature.Wish,
        AddContainerFeature.State,
        Nothing
        > {
        return AddContainerFeature(
            initialState = initialState,
            actor = AddContainerActor(
                measurementRepository,
                mnoRepository,
                schedulers
            ),
            reducer = AddContainerReducer()
        )
    }

    @Provides
    fun provideBinder(): Binder {
        return Binder()
    }
}
