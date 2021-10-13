package ru.ktsstudio.app_verification.domain.object_survey.tech

import ru.ktsstudio.app_verification.domain.object_survey.common.VerificationObjectDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey

/**
 * @author Maxim Ovchinnikov on 11.12.2020.
 */
class TechnicalDraftMerger : VerificationObjectDraftMerger<TechnicalSurveyDraft> {
    override fun invoke(
        verificationObjectWithSurvey: VerificationObjectWithCheckedSurvey,
        draft: TechnicalSurveyDraft
    ): VerificationObjectWithCheckedSurvey {
        return verificationObjectWithSurvey.copy(
            verificationObject = verificationObjectWithSurvey.verificationObject.copy(
                survey = verificationObjectWithSurvey.verificationObject.survey.copyTechnicalSurvey(
                    draft.technicalSurvey
                )
            ),
            checkedSurvey = verificationObjectWithSurvey.checkedSurvey.copyTechnicalCheckedSurvey(
                draft.technicalCheckedSurvey
            )
        )
    }
}
