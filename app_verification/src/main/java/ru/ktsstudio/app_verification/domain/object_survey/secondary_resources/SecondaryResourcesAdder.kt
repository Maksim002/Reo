package ru.ktsstudio.app_verification.domain.object_survey.secondary_resources

import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectAdder
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.secondaryResources
import ru.ktsstudio.app_verification.presentation.object_survey.secondary_resources.SecondaryResourceEntity
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourceType
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.types
import java.util.UUID

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class SecondaryResourcesAdder : NestedObjectAdder<SecondaryResourcesSurveyDraft, SecondaryResourceEntity> {
    override fun invoke(
        draft: SecondaryResourcesSurveyDraft,
        entityType: SecondaryResourceEntity
    ): SecondaryResourcesSurveyDraft {
        val id = generateId(entityType)
        return SecondaryResourcesSurveyDraft.secondaryResources.types.modify(draft) {
            it.plus(id to SecondaryResourceType.createEmpty(id)).k()
        }
    }

    private fun generateId(entityType: SecondaryResourceEntity): String = UUID.randomUUID().toString()
}
