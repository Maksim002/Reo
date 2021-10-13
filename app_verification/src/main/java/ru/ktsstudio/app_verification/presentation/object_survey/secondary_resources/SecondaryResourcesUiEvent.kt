package ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources

import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectHolderFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.reference.ReferenceFeature
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesSurveyDraft
import ru.ktsstudio.app_verification.ui.common.Updater

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
internal sealed class SecondaryResourcesUiEvent {
    data class Load(val objectId: String) : SecondaryResourcesUiEvent()
    data class UpdateSurvey(val updater: Updater<SecondaryResourcesSurveyDraft>) : SecondaryResourcesUiEvent()
    data class SetSurvey(val draft: SecondaryResourcesSurveyDraft) : SecondaryResourcesUiEvent()
    data class SaveSurvey(val objectId: String) : SecondaryResourcesUiEvent()
    data class AddSecondaryResourceEntity(val entity: SecondaryResourceEntity) : SecondaryResourcesUiEvent()
    data class DeleteSecondaryResourceEntity(
        val id: String,
        val entity: SecondaryResourceEntity
    ) : SecondaryResourcesUiEvent()

    fun toSurveyFeatureWish(): ObjectSurveyFeature.Wish<SecondaryResourcesSurveyDraft>? {
        return when (this) {
            is Load -> ObjectSurveyFeature.Wish.Load(objectId)
            is UpdateSurvey -> ObjectSurveyFeature.Wish.UpdateSurvey(updater)
            is SetSurvey -> ObjectSurveyFeature.Wish.SetSurvey(draft)
            is SaveSurvey -> ObjectSurveyFeature.Wish.SaveSurvey(objectId)
            is AddSecondaryResourceEntity -> null
            is DeleteSecondaryResourceEntity -> null
        }
    }

    fun toNestedObjectFeatureWish():
        NestedObjectHolderFeature.Wish<SecondaryResourcesSurveyDraft, SecondaryResourceEntity>? {
        return when (this) {
            is Load -> null
            is UpdateSurvey -> null
            is SetSurvey -> NestedObjectHolderFeature.Wish.SetDraft(draft)
            is SaveSurvey -> null
            is AddSecondaryResourceEntity -> NestedObjectHolderFeature.Wish.Add(entity)
            is DeleteSecondaryResourceEntity -> NestedObjectHolderFeature.Wish.Delete(id, entity)
        }
    }

    fun toReferenceFeatureWish(): ReferenceFeature.Wish? {
        return when (this) {
            is Load -> ReferenceFeature.Wish.Load
            is UpdateSurvey -> null
            is SetSurvey -> null
            is SaveSurvey -> null
            is AddSecondaryResourceEntity -> null
            is DeleteSecondaryResourceEntity -> null
        }
    }
}
