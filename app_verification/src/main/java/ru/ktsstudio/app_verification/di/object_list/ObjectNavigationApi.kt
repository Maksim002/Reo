package ru.ktsstudio.app_verification.di.object_list

import ru.ktsstudio.app_verification.navigation.ObjectNavigator

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
interface ObjectNavigationApi {
    fun objectNavigation(): ObjectNavigator
}
