package ru.ktsstudio.settings.di

import ru.ktsstudio.settings.navigation.SettingsNavigator

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
interface SettingsNavigationApi {
    fun settingsNavigation(): SettingsNavigator
}