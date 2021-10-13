package ru.ktsstudio.feature_map.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.feature_map.data.CameraPositionStore
import ru.ktsstudio.feature_map.data.CameraPositionStoreImpl
import ru.ktsstudio.feature_map.presentation.MapViewModel
import javax.inject.Provider

/**
 * Created by Igor Park on 04/10/2020.
 */
@Module
internal abstract class MapModule {

    @Binds
    abstract fun bindCameraPositionStore(impl: CameraPositionStoreImpl): CameraPositionStore

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindViewModel(impl: MapViewModel): ViewModel

    companion object {
        @Provides
        @JvmStatic
        fun provideViewModelProvider(
            viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
        ): ViewModelProvider.Factory {
            return ViewModelFactory(viewModelsMap)
        }
    }
}