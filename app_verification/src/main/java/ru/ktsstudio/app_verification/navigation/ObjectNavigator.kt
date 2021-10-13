package ru.ktsstudio.app_verification.navigation

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
interface ObjectNavigator {
    fun openObjectInspection(objectId: String)
    fun openObjectFilter()
}
