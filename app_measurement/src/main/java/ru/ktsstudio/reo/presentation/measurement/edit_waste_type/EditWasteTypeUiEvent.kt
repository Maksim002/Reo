package ru.ktsstudio.reo.presentation.measurement.edit_waste_type

import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.edit_waste_type.EditWasteTypeFeature
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
sealed class EditWasteTypeUiEvent {

    data class InitData(val wasteTypeId: String?) : EditWasteTypeUiEvent()

    data class UpdateField(
        val dataType: ContainerDataType,
        val value: String
    ) : EditWasteTypeUiEvent()

    data class UpdateForm(
        val form: MeasurementForm
    ) : EditWasteTypeUiEvent()

    data class SwitchField(
        val dataType: ContainerDataType,
        val hasFocus: Boolean
    ) : EditWasteTypeUiEvent()

    data class SaveData(val measurementId: Long, val wasteTypeId: String?) : EditWasteTypeUiEvent()
    data class DeleteData(val containerId: Long, val wasteTypeId: String) : EditWasteTypeUiEvent()

    fun toEditWasteTypeFeatureWish(): EditWasteTypeFeature.Wish? {
        return when (this) {
            is InitData -> EditWasteTypeFeature.Wish.InitData(wasteTypeId)
            is UpdateField -> EditWasteTypeFeature.Wish.UpdateField(dataType, value)
            is SaveData -> EditWasteTypeFeature.Wish.SaveData(measurementId, wasteTypeId)
            is DeleteData -> EditWasteTypeFeature.Wish.DeleteData(containerId, wasteTypeId)
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
