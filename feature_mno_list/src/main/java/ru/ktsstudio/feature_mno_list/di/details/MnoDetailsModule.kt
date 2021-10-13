package ru.ktsstudio.feature_mno_list.di.details

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
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.feature_mno_list.domain.details.MnoDetailsActor
import ru.ktsstudio.feature_mno_list.domain.details.MnoDetailsFeature
import ru.ktsstudio.feature_mno_list.domain.details.MnoDetailsReducer
import ru.ktsstudio.feature_mno_list.presentation.details.MnoDetailsViewModel

/**
 * @author Maxim Myalkin (MaxMyalkin) on 01.10.2020.
 */

@Module
internal interface MnoDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MnoDetailsViewModel::class)
    fun bindViewModel(impl: MnoDetailsViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideMnoDetailsFeature(
            initialState: MnoDetailsFeature.State,
            mnoRepository: MnoRepository,
            measurementRepository: MeasurementRepository,
            schedulerProvider: SchedulerProvider
        ): Feature<
                MnoDetailsFeature.Wish,
                MnoDetailsFeature.State,
                Nothing
                > {
            return MnoDetailsFeature(
                initialState = initialState,
                actor = MnoDetailsActor(mnoRepository, measurementRepository, schedulerProvider),
                reducer = MnoDetailsReducer()
            )
        }

        @Provides
        @JvmStatic
        fun provideInitialState(): MnoDetailsFeature.State {
            return MnoDetailsFeature.State()
        }

        @Provides
        @JvmStatic
        fun provideBinder(): Binder {
            return Binder()
        }
    }

}