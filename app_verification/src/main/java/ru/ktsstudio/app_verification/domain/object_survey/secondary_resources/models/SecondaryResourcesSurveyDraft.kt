package ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models

import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesSurvey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
@optics
data class SecondaryResourcesSurveyDraft(
    val secondaryResources: SecondaryResourcesSurvey,
    val checkedSecondaryResources: SecondaryResourcesCheckedSurvey
) {
    companion object
}
