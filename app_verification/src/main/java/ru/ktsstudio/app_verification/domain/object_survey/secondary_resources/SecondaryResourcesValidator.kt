package ru.ktsstudio.app_verification.domain.object_survey.secondary_resources

import ru.ktsstudio.app_verification.domain.object_survey.common.SurveyDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesSurveyDraft
import ru.ktsstudio.common.utils.orDefault
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourceType

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.12.2020.
 */
class SecondaryResourcesValidator(
    private val fillValidator: FillValidator<SecondaryResourceType>
) : SurveyDraftValidator<SecondaryResourcesSurveyDraft> {
    override fun invoke(draft: SecondaryResourcesSurveyDraft): Boolean {
        val overallPercent = draft.secondaryResources.extractPercent.orDefault(0f)
        val types = draft.secondaryResources.types.values
        return types.all(fillValidator::isFilled) &&
            types.map { it.percent.orDefault(0f) }.sum() <= overallPercent
    }
}
