package ru.ktsstudio.feature_auth.di.flow.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import javax.inject.Provider

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
@Module
internal object AuthModule {

    @Provides
    fun provideViewModelProvider(
        viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return ViewModelFactory(viewModelsMap)
    }
}