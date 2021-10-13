package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure

import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.InfrastructureEquipmentType
import ru.ktsstudio.app_verification.ui.common.Updater

internal sealed class InfrastructureSurveyUiEvent {

    data class LoadSurvey(val objectId: String) : InfrastructureSurveyUiEvent()
    data class UpdateSurvey(
        val updater: Updater<InfrastructureSurveyDraft>
    ) : InfrastructureSurveyUiEvent()

    data class SaveSurvey(val objectId: String) : InfrastructureSurveyUiEvent()
    data class SetSurvey(val draft: InfrastructureSurveyDraft) : InfrastructureSurveyUiEvent()
    data class AddEquipmentEntity(
        val entity: InfrastructureEquipmentType
    ) : InfrastructureSurveyUiEvent()

    data class DeleteEquipmentEntity(
        val id: String,
        val entity: InfrastructureEquipmentType
    ) : InfrastructureSurveyUiEvent()

    fun toSurveyFeatureWish(): ObjectSurveyFeature.Wish<InfrastructureSurveyDraft>? {
        return when (this) {
            is LoadSurvey -> ObjectSurveyFeature.Wish.Load(objectId)
            is UpdateSurvey -> ObjectSurveyFeature.Wish.UpdateSurvey(updater)
            is SaveSurvey -> ObjectSurveyFeature.Wish.SaveSurvey(objectId)
            is SetSurvey -> ObjectSurveyFeature.Wish.SetSurvey(draft)
            is AddEquipmentEntity -> null
            is DeleteEquipmentEntity -> null
        }
    }

    fun toReferenceFeatureWish(): ReferenceFeature.Wish? {
        return when (this) {
            is LoadSurvey -> ReferenceFeature.Wish.Load
            is UpdateSurvey,
            is SetSurvey,
            is AddEquipmentEntity,
            is DeleteEquipmentEntity,
            is SaveSurvey -> null
        }
    }

    fun toNestedObjectFeatureWish(): NestedObjectHolderFeature.Wish<
        InfrastructureSurveyDraft,
        InfrastructureEquipmentType
        >? {
        return when (this) {
            is LoadSurvey -> null
            is UpdateSurvey -> null
            is SaveSurvey -> null
            is SetSurvey -> NestedObjectHolderFeature.Wish.SetDraft(draft)
            is AddEquipmentEntity -> NestedObjectHolderFeature.Wish.Add(entity)
            is DeleteEquipmentEntity -> NestedObjectHolderFeature.Wish.Delete(id, entity)
        }
    }
}
