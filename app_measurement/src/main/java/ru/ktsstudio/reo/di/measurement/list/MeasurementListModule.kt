package ru.ktsstudio.reo.di.measurement.list

import androidx.lifecycle.ViewModel
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.domain.filter.FilterUpdater
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.reo.di.filter.MeasurementFilter
import ru.ktsstudio.reo.domain.measurement.MeasurementFilterApplier
import ru.ktsstudio.reo.domain.measurement.list.MeasurementListActor
import ru.ktsstudio.reo.domain.measurement.list.MeasurementListFeature
import ru.ktsstudio.reo.domain.measurement.list.MeasurementListReducer
import ru.ktsstudio.reo.presentation.measurement.list.MeasurementListViewModel

/**
 * @author Maxim Ovchinnikov on 08.10.2020.
 */
@Module
internal interface MeasurementListModule {

    @Binds
    @IntoMap
    @ViewModelKey(MeasurementListViewModel::class)
    fun bindViewModel(impl: MeasurementListViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideMeasurementListFeature(
            measurementRepository: MeasurementRepository,
            mnoRepository: MnoRepository,
            schedulers: SchedulerProvider,
            measurementFilterApplier: MeasurementFilterApplier,
            @MeasurementFilter filterUpdater: FilterUpdater
        ): Feature<
            MeasurementListFeature.Wish,
            MeasurementListFeature.State,
            Nothing
            > {
            return MeasurementListFeature(
                initialState = MeasurementListFeature.State(),
                actor = MeasurementListActor(
                    measurementRepository = measurementRepository,
                    mnoRepository = mnoRepository,
                    schedulers = schedulers,
                    measurementFilterApplier = measurementFilterApplier,
                    filterUpdater = filterUpdater
                ),
                reducer = MeasurementListReducer()
            )
        }

        @Provides
        @JvmStatic
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
