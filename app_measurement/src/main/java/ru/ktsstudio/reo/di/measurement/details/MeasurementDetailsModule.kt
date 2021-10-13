package ru.ktsstudio.reo.di.measurement.details

import androidx.lifecycle.ViewModel
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.reo.domain.measurement.common.MeasurementDraftHolder
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsActor
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsNewsPublisher
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsReducer
import ru.ktsstudio.reo.presentation.measurement.details.MeasurementDetailsViewModel

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */

@Module
internal interface MeasurementDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MeasurementDetailsViewModel::class)
    fun bindViewModel(impl: MeasurementDetailsViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideMeasurementDetailsFeature(
            initialState: MeasurementDetailsFeature.State,
            measurementRepository: MeasurementRepository,
            schedulerProvider: SchedulerProvider,
            draftHolder: MeasurementDraftHolder?
        ): Feature<
            MeasurementDetailsFeature.Wish,
            MeasurementDetailsFeature.State,
            MeasurementDetailsFeature.News
            > {
            return MeasurementDetailsFeature(
                initialState = initialState,
                actor = MeasurementDetailsActor(
                    measurementRepository,
                    schedulerProvider,
                    draftHolder
                ),
                reducer = MeasurementDetailsReducer(),
                newsPublisher = MeasurementDetailsNewsPublisher()
            )
        }

        @Provides
        @JvmStatic
        fun provideInitialState(): MeasurementDetailsFeature.State {
            return MeasurementDetailsFeature.State()
        }

        @Provides
        @JvmStatic
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
