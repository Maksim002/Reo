package ru.ktsstudio.reo.di.measurement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import javax.inject.Provider

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
@Module
internal object MeasurementCommonModule {

    @Provides
    fun provideViewModelProvider(
        viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return ViewModelFactory(viewModelsMap)
    }
}
