package ru.ktsstudio.reo.di.create_measurement.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.reo.presentation.measurement.start.StartMeasurementViewModel
import javax.inject.Provider

/**
 * Created by Igor Park on 13/10/2020.
 */
@Module
interface StartMeasurementModule {
    @Binds
    @IntoMap
    @ViewModelKey(StartMeasurementViewModel::class)
    fun bindViewModel(impl: StartMeasurementViewModel): ViewModel

    companion object {
        @Provides
        fun provideViewModelProvider(
            viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory {
            return ViewModelFactory(viewModelsMap)
        }
    }
}
