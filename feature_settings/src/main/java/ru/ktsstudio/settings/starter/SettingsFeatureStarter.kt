package ru.ktsstudio.settings.starter

import androidx.fragment.app.Fragment
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.di.CoreAuthApi
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.settings.di.SettingsComponent
import ru.ktsstudio.settings.di.SettingsNavigationApi
import ru.ktsstudio.settings.ui.SettingsFragment

/**
 * @author Maxim Ovchinnikov on 11.11.2020.
 */
object SettingsFeatureStarter {
    fun start(
        coreApi: CoreApi,
        coreAuthApi: CoreAuthApi,
        settingsNavigationApi: SettingsNavigationApi,
        settingsRepository: SettingsRepository
    ): Fragment {
        ComponentRegistry.register {
            SettingsComponent.create(coreApi, settingsNavigationApi, coreAuthApi, settingsRepository)
        }
        return SettingsFragment()
    }
}