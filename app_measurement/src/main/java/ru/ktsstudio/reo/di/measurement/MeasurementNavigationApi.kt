package ru.ktsstudio.reo.di.measurement

import ru.ktsstudio.reo.navigation.MeasurementNavigator

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
interface MeasurementNavigationApi {
    fun measurementNavigation(): MeasurementNavigator
}
