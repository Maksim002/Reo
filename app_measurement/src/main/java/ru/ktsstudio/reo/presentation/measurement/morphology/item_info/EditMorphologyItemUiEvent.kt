package ru.ktsstudio.reo.presentation.measurement.morphology.item_info

import ru.ktsstudio.form_feature.FormFeature
import ru.ktsstudio.reo.domain.measurement.edit_mixed_container.models.ContainerDataType
import ru.ktsstudio.reo.domain.measurement.form.MeasurementForm
import ru.ktsstudio.reo.domain.measurement.morphology.item_info.EditMorphologyItemFeature

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.11.2020.
 */
sealed class EditMorphologyItemUiEvent {

    data class InitData(val categoryId: Long?) : EditMorphologyItemUiEvent()

    data class UpdateField(
        val dataType: ContainerDataType,
        val value: String
    ) : EditMorphologyItemUiEvent()

    data class SaveData(val measurementId: Long, val categoryId: Long?) : EditMorphologyItemUiEvent()
    data class DeleteData(val categoryId: Long) : EditMorphologyItemUiEvent()

    data class UpdateForm(
        val form: MeasurementForm
    ) : EditMorphologyItemUiEvent()

    data class SwitchField(
        val dataType: ContainerDataType,
        val hasFocus: Boolean
    ) : EditMorphologyItemUiEvent()

    fun toEditMorphologyItemFeatureWish(): EditMorphologyItemFeature.Wish? {
        return when (this) {
            is InitData -> EditMorphologyItemFeature.Wish.InitData(categoryId)
            is UpdateField -> EditMorphologyItemFeature.Wish.UpdateField(dataType, value)
            is SaveData -> EditMorphologyItemFeature.Wish.SaveData(measurementId, categoryId)
            is DeleteData -> EditMorphologyItemFeature.Wish.DeleteData(categoryId)
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
