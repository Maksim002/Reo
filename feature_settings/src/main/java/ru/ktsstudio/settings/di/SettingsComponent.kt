package ru.ktsstudio.settings.di

import dagger.Component
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.CoreAuthApi
import ru.ktsstudio.common.di.FeatureScope
import ru.ktsstudio.settings.ui.SettingsFragment

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
@Component(
    dependencies = [CoreApi::class, SettingsNavigationApi::class, CoreAuthApi::class, SettingsRepository::class],
    modules = [SettingsModule::class]
)
@FeatureScope
internal interface SettingsComponent {

    fun inject(fragment: SettingsFragment)

    companion object {
        fun create(
            coreApi: CoreApi,
            settingsNavigationApi: SettingsNavigationApi,
            coreAuthApi: CoreAuthApi,
            settingsRepository: SettingsRepository,
        ): SettingsComponent = DaggerSettingsComponent.builder()
            .coreApi(coreApi)
            .coreAuthApi(coreAuthApi)
            .settingsNavigationApi(settingsNavigationApi)
            .settingsRepository(settingsRepository)
            .build()
    }
}