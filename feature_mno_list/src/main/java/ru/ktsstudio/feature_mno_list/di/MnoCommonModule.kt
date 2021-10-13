package ru.ktsstudio.feature_mno_list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import javax.inject.Provider

/**
 * @author Maxim Myalkin (MaxMyalkin) on 01.10.2020.
 */
@Module
internal object MnoCommonModule {

    @Provides
    @JvmStatic
    fun provideViewModelProvider(
        viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return ViewModelFactory(viewModelsMap)
    }

}