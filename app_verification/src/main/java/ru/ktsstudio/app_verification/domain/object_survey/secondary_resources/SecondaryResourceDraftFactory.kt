package ru.ktsstudio.app_verification.domain.object_survey.secondary_resources

import ru.ktsstudio.app_verification.domain.object_survey.common.DraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
class SecondaryResourceDraftFactory : DraftFactory<SecondaryResourcesSurveyDraft> {
    override fun invoke(
        verificationObjectWithCheckedSurvey: VerificationObjectWithCheckedSurvey
    ): SecondaryResourcesSurveyDraft {
        return SecondaryResourcesSurveyDraft(
            secondaryResources = verificationObjectWithCheckedSurvey.verificationObject
                .survey
                .let { it as? Survey.WasteTreatmentSurvey }
                ?.secondaryResourcesSurvey
                ?: error("secondary resource is available only in WasteTreatmentSurvey"),
            checkedSecondaryResources = verificationObjectWithCheckedSurvey
                .checkedSurvey
                .let { it as? CheckedSurvey.WasteTreatmentCheckedSurvey }
                ?.secondaryResourcesCheckedSurvey
                ?: error("secondary resource is available only in WasteTreatmentSurvey"),
        )
    }
}
