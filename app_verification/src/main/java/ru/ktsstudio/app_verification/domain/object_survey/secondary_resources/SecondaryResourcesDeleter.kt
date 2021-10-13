package ru.ktsstudio.app_verification.domain.object_survey.secondary_resources

import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectDeleter
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.secondaryResources
import ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources.SecondaryResourceEntity
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.types

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class SecondaryResourcesDeleter : NestedObjectDeleter<SecondaryResourcesSurveyDraft, SecondaryResourceEntity> {

    override fun invoke(
        draft: SecondaryResourcesSurveyDraft,
        id: String,
        entityType: SecondaryResourceEntity
    ): SecondaryResourcesSurveyDraft {
        return SecondaryResourcesSurveyDraft.secondaryResources.types.modify(draft) {
            it.minus(id).k()
        }
    }
}
