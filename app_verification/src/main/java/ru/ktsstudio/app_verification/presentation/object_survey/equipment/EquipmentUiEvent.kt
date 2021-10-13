package ru.ktsstudio.app_verification.presentation.object_survey.equipment

import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.ui.common.Updater

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
internal sealed class EquipmentUiEvent {
    data class Load(val objectId: String) : EquipmentUiEvent()
    data class UpdateSurvey(val updater: Updater<EquipmentSurveyDraft>) : EquipmentUiEvent()
    data class SetSurvey(val draft: EquipmentSurveyDraft) : EquipmentUiEvent()
    data class SaveSurvey(val objectId: String) : EquipmentUiEvent()
    data class AddEquipmentEntity(val entity: EquipmentEntity) : EquipmentUiEvent()
    data class DeleteEquipmentEntity(val id: String, val entity: EquipmentEntity) :
        EquipmentUiEvent()

    fun toSurveyFeatureWish(): ObjectSurveyFeature.Wish<EquipmentSurveyDraft>? {
        return when (this) {
            is Load -> ObjectSurveyFeature.Wish.Load(objectId)
            is UpdateSurvey -> ObjectSurveyFeature.Wish.UpdateSurvey(updater)
            is SetSurvey -> ObjectSurveyFeature.Wish.SetSurvey(draft)
            is SaveSurvey -> ObjectSurveyFeature.Wish.SaveSurvey(objectId)
            is AddEquipmentEntity -> null
            is DeleteEquipmentEntity -> null
        }
    }

    fun toNestedObjectFeatureWish(): NestedObjectHolderFeature.Wish<EquipmentSurveyDraft, EquipmentEntity>? {
        return when (this) {
            is SetSurvey -> NestedObjectHolderFeature.Wish.SetDraft(draft)
            is AddEquipmentEntity -> NestedObjectHolderFeature.Wish.Add(entity)
            is DeleteEquipmentEntity -> NestedObjectHolderFeature.Wish.Delete(id, entity)
            is UpdateSurvey -> null
            is Load -> null
            is SaveSurvey -> null
        }
    }

    fun toReferenceFeatureWish(): ReferenceFeature.Wish? {
        return when (this) {
            is Load -> ReferenceFeature.Wish.Load
            is UpdateSurvey,
            is SetSurvey,
            is SaveSurvey,
            is AddEquipmentEntity,
            is DeleteEquipmentEntity -> null
        }
    }
}
