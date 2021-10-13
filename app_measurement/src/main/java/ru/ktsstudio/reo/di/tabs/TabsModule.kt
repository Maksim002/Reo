package ru.ktsstudio.reo.di.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.data.network.state.NetworkStateProvider
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.reo.domain.tabs.TabsActor
import ru.ktsstudio.reo.domain.tabs.TabsFeature
import ru.ktsstudio.reo.domain.tabs.TabsNewsPublisher
import ru.ktsstudio.reo.domain.tabs.TabsReducer
import ru.ktsstudio.reo.presentation.tabs.TabsViewModel
import javax.inject.Provider

/**
 * @author Maxim Ovchinnikov on 01.11.2020.
 */
@Module
internal interface TabsModule {

    @Binds
    @IntoMap
    @ViewModelKey(TabsViewModel::class)
    fun bindViewModel(impl: TabsViewModel): ViewModel

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
        fun provideTabsFeature(
            networkStateProvider: NetworkStateProvider
        ): Feature<
            TabsFeature.Wish,
            Unit,
            TabsFeature.News
            > {
            return TabsFeature(
                actor = TabsActor(networkStateProvider),
                reducer = TabsReducer(),
                newsPublisher = TabsNewsPublisher()
            )
        }

        @Provides
        @JvmStatic
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}
