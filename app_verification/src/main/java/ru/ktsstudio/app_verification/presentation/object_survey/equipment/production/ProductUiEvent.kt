package ru.ktsstudio.app_verification.presentation.object_survey.equipment.production

import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.ui.common.Updater

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
internal sealed class ProductUiEvent {
    data class Load(val objectId: String) : ProductUiEvent()
    data class UpdateSurvey(val updater: Updater<ProductionSurveyDraft>) : ProductUiEvent()
    data class SetSurvey(val draft: ProductionSurveyDraft) : ProductUiEvent()
    data class SaveSurvey(val objectId: String) : ProductUiEvent()
    data class AddEquipmentEntity(val entity: ProducedEntityType) : ProductUiEvent()
    data class DeleteEquipmentEntity(val id: String, val entity: ProducedEntityType) : ProductUiEvent()

    fun toSurveyFeatureWish(): ObjectSurveyFeature.Wish<ProductionSurveyDraft>? {
        return when (this) {
            is Load -> ObjectSurveyFeature.Wish.Load(objectId)
            is UpdateSurvey -> ObjectSurveyFeature.Wish.UpdateSurvey(updater)
            is SetSurvey -> ObjectSurveyFeature.Wish.SetSurvey(draft)
            is SaveSurvey -> ObjectSurveyFeature.Wish.SaveSurvey(objectId)
            is AddEquipmentEntity -> null
            is DeleteEquipmentEntity -> null
        }
    }

    fun toNestedObjectFeatureWish(): NestedObjectHolderFeature.Wish<ProductionSurveyDraft, ProducedEntityType>? {
        return when (this) {
            is SetSurvey -> NestedObjectHolderFeature.Wish.SetDraft(draft)
            is AddEquipmentEntity -> NestedObjectHolderFeature.Wish.Add(entity)
            is DeleteEquipmentEntity -> NestedObjectHolderFeature.Wish.Delete(id, entity)
            is UpdateSurvey -> null
            is Load -> null
            is SaveSurvey -> null
        }
    }
}
