package ru.ktsstudio.app_verification.domain.object_survey.tech

import ru.ktsstudio.app_verification.domain.object_survey.common.DraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey

/**
 * @author Maxim Ovchinnikov on 11.12.2020.
 */
class TechnicalDraftFactory : DraftFactory<TechnicalSurveyDraft> {

    override fun invoke(
        verificationObjectWIthCheckedSurvey: VerificationObjectWithCheckedSurvey
    ): TechnicalSurveyDraft {
        return TechnicalSurveyDraft(
            technicalSurvey = verificationObjectWIthCheckedSurvey.verificationObject.survey.technicalSurvey,
            technicalCheckedSurvey = verificationObjectWIthCheckedSurvey.checkedSurvey.technicalCheckedSurvey
        )
    }
}
