package ru.ktsstudio.reo.presentation.measurement.edit_mixed_container

import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.EditMixedContainerFeature
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
sealed class EditMixedContainerUiEvent {

    data class InitData(
        val containerId: Long?,
        val mnoContainerId: String?,
        val containerTypeId: String?
    ) : EditMixedContainerUiEvent()

    data class UpdateField(
        val dataType: ContainerDataType,
        val value: String
    ) : EditMixedContainerUiEvent()

    data class SaveData(val measurementId: Long, val containerId: Long?) : EditMixedContainerUiEvent()
    data class DeleteData(val containerId: Long) : EditMixedContainerUiEvent()

    data class SwitchField(
        val dataType: ContainerDataType,
        val hasFocus: Boolean
    ) : EditMixedContainerUiEvent()

    data class UpdateForm(
        val form: MeasurementForm
    ) : EditMixedContainerUiEvent()

    fun toEditMixedContainerFeatureWish(): EditMixedContainerFeature.Wish? {
        return when (this) {
            is InitData -> EditMixedContainerFeature.Wish.InitData(containerId, mnoContainerId, containerTypeId)
            is UpdateField -> EditMixedContainerFeature.Wish.UpdateField(dataType, value)
            is SaveData -> EditMixedContainerFeature.Wish.SaveData(measurementId, containerId)
            is DeleteData -> EditMixedContainerFeature.Wish.DeleteData(containerId)
            is SwitchField -> null
            is UpdateForm -> null
        }
    }

    fun toFormFeatureWish(): FormFeature.Wish<MeasurementForm>? {
        return when (this) {
            is InitData -> null
            is UpdateField -> null
            is SaveData -> null
            is DeleteData -> null
            is SwitchField -> FormFeature.Wish.SwitchField(dataType, hasFocus)
            is UpdateForm -> FormFeature.Wish.Input(form)
        }
    }
}
