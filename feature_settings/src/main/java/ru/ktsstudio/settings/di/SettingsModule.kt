package ru.ktsstudio.settings.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.feature.Feature
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.data.auth.AuthRepository
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.common.ui.view_model.ViewModelFactory
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.settings.domain.SettingsActor
import ru.ktsstudio.settings.domain.SettingsFeature
import ru.ktsstudio.settings.domain.SettingsNewsPublisher
import ru.ktsstudio.settings.domain.SettingsReducer
import ru.ktsstudio.settings.presentation.SettingsViewModel
import javax.inject.Provider

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
@Module
internal interface SettingsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindViewModel(impl: SettingsViewModel): ViewModel

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
        fun provideSettingsFeature(
            settingsRepository: SettingsRepository,
            authRepository: AuthRepository,
            schedulerProvider: SchedulerProvider
        ): Feature<
            SettingsFeature.Wish,
            SettingsFeature.State,
            SettingsFeature.News
            > {
            return SettingsFeature(
                initialState = SettingsFeature.State(),
                actor = SettingsActor(settingsRepository, authRepository, schedulerProvider),
                reducer = SettingsReducer(),
                newsPublisher = SettingsNewsPublisher()
            )
        }

        @Provides
        @JvmStatic
        fun provideBinder(): Binder {
            return Binder()
        }
    }
}