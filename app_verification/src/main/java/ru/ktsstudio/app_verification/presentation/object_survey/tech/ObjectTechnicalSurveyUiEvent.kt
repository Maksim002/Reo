package ru.ktsstudio.app_verification.presentation.object_survey.tech

import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.TechnicalCardType
import ru.ktsstudio.app_verification.ui.common.Updater

/**
 * @author Maxim Ovchinnikov on 14.12.2020.
 */
internal sealed class ObjectTechnicalSurveyUiEvent {

    data class LoadSurvey(val objectId: String) : ObjectTechnicalSurveyUiEvent()
    data class UpdateSurvey(
        val updater: Updater<TechnicalSurveyDraft>
    ) : ObjectTechnicalSurveyUiEvent()
    data class SaveSurvey(val objectId: String) : ObjectTechnicalSurveyUiEvent()

    data class SetSurvey(val draft: TechnicalSurveyDraft) : ObjectTechnicalSurveyUiEvent()
    data class AddEquipmentEntity(
        val entity: TechnicalCardType
    ) : ObjectTechnicalSurveyUiEvent()
    data class DeleteEquipmentEntity(
        val id: String,
        val entity: TechnicalCardType
    ) : ObjectTechnicalSurveyUiEvent()

    fun toSurveyFeatureWish(): ObjectSurveyFeature.Wish<TechnicalSurveyDraft>? {
        return when (this) {
            is LoadSurvey -> ObjectSurveyFeature.Wish.Load(objectId)
            is UpdateSurvey -> ObjectSurveyFeature.Wish.UpdateSurvey(updater)
            is SaveSurvey -> ObjectSurveyFeature.Wish.SaveSurvey(objectId)
            is SetSurvey -> ObjectSurveyFeature.Wish.SetSurvey(draft)
            is AddEquipmentEntity,
            is DeleteEquipmentEntity -> null
        }
    }

    fun toNestedObjectFeatureWish(): NestedObjectHolderFeature.Wish<
        TechnicalSurveyDraft,
        TechnicalCardType
        >? {
        return when (this) {
            is SetSurvey -> NestedObjectHolderFeature.Wish.SetDraft(draft)
            is AddEquipmentEntity -> NestedObjectHolderFeature.Wish.Add(entity)
            is DeleteEquipmentEntity -> NestedObjectHolderFeature.Wish.Delete(id, entity)
            is LoadSurvey,
            is UpdateSurvey,
            is SaveSurvey -> null
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
}
