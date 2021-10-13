package ru.ktsstudio.reo.di.map.mno_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MeasurementRepository
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.reo.domain.map.mno_info.MnoInfoActor
import ru.ktsstudio.reo.domain.map.mno_info.MnoInfoFeature
import ru.ktsstudio.reo.domain.map.mno_info.MnoInfoReducer
import ru.ktsstudio.reo.presentation.map.MnoInfoViewModel
import javax.inject.Provider

@Module
abstract class MnoInfoModule {

    @Binds
    @IntoMap
    @ViewModelKey(MnoInfoViewModel::class)
    abstract fun bindViewModel(impl: MnoInfoViewModel): ViewModel

    companion object {
        @Provides
        @JvmStatic
        fun provideViewModelProvider(
            viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory {
            return ViewModelFactory(viewModelsMap)
        }

        @Provides
        @JvmStatic
        fun provideMapFeature(
            mnoRepository: MnoRepository,
            measurementRepository: MeasurementRepository,
            schedulers: SchedulerProvider
        ): Feature<
            MnoInfoFeature.Wish,
            MnoInfoFeature.State,
            Nothing
            > {
            return MnoInfoFeature(
                initialState = MnoInfoFeature.State(
                    isLoading = false,
                    error = null,
                    infoList = emptyList()
                ),
                actor = MnoInfoActor(mnoRepository, measurementRepository, schedulers),
                reducer = MnoInfoReducer()
            )
        }

        @Provides
        @JvmStatic
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
