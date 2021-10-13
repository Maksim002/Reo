package ru.ktsstudio.app_verification.domain.object_survey.secondary_resources

import ru.ktsstudio.app_verification.domain.object_survey.common.VerificationObjectDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.secondary_resources.models.SecondaryResourcesSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */

class SecondaryResourceDraftMerger : VerificationObjectDraftMerger<SecondaryResourcesSurveyDraft> {
    override fun invoke(
        verificationObjectWithSurvey: VerificationObjectWithCheckedSurvey,
        draft: SecondaryResourcesSurveyDraft
    ): VerificationObjectWithCheckedSurvey {
        val treatmentSurvey = verificationObjectWithSurvey.verificationObject
            .survey
            .let { it as? Survey.WasteTreatmentSurvey }
            ?: error("secondary resource is available only in WasteTreatmentSurvey")

        val treatmentCheckedSurvey = verificationObjectWithSurvey.checkedSurvey
            .let { it as? CheckedSurvey.WasteTreatmentCheckedSurvey }
            ?: error("secondary resource is available only in WasteTreatmentSurvey")

        return verificationObjectWithSurvey.copy(
            verificationObject = verificationObjectWithSurvey.verificationObject.copy(
                survey = treatmentSurvey.copy(
                    secondaryResourcesSurvey = draft.secondaryResources
                )
            ),
            checkedSurvey = treatmentCheckedSurvey.copy(
                secondaryResourcesCheckedSurvey = draft.checkedSecondaryResources
            )
        )
    }
}
