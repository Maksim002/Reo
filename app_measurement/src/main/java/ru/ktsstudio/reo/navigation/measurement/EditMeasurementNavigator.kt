package ru.ktsstudio.reo.navigation.measurement

/**
 * Created by Igor Park on 11/10/2020.
 */
interface EditMeasurementNavigator {
    fun measurementSkipped()
    fun measurementCreateCard(mnoId: String)

    fun measurementPreviewCard(measurementId: Long, returnTag: MeasurementReturnTag)
    fun measurementAddContainer(mnoId: String, measurementId: Long)
    fun measurementEditMorphology(measurementId: Long)
    fun measurementEditMorphologyItem(measurementId: Long, morphologyItemId: Long?)
    fun measurementEditMorphologyCompleted()
    fun measurementEditMorphologyItemCompleted()

    fun measurementAddSeparateContainer(navigationData: EditContainerNavigationData)
    fun measurementAddMixedContainer(navigationData: EditContainerNavigationData)

    fun measurementEditSeparateContainer(measurementId: Long, containerId: Long)
    fun measurementEditMixedContainer(measurementId: Long, containerId: Long)

    data class EditContainerNavigationData(
        val measurementId: Long,
        val mnoContainerId: String?,
        val containerTypeId: String?
    )

    fun measurementContainerUpdated()
    fun measurementWasteTypeUpdated()
    fun measurementEditWasteType(separateContainerId: Long, wasteTypeId: String?)
    fun measurementConfirmDataClear(actionKey: String)
    fun measurementGpsEnableRequest(message: String)
}
