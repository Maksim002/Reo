package ru.ktsstudio.reo.navigation

import ru.ktsstudio.reo.navigation.measurement.MeasurementReturnTag

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
interface MeasurementNavigator {
    fun openMeasurementDetails(localId: Long)
    fun measurementDetailsEditMeasurement(mnoId: String, measurementId: Long)
    fun measurementDetailsEditComplete(returnTag: MeasurementReturnTag)
}
