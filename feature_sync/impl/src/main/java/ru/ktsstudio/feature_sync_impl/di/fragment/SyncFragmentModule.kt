package ru.ktsstudio.feature_sync_impl.di.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.feature_sync_impl.presentation.SyncViewModel
import javax.inject.Provider

@Module
interface SyncFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(SyncViewModel::class)
    fun bindViewModel(impl: SyncViewModel): ViewModel

    companion object Static {

        @Provides
        fun provideViewModelProvider(
            viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory = ViewModelFactory(viewModelsMap)
    }
}