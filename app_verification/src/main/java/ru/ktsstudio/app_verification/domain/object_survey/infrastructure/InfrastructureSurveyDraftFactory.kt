package ru.ktsstudio.app_verification.domain.object_survey.infrastructure

import ru.ktsstudio.app_verification.domain.object_survey.common.DraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey

class InfrastructureSurveyDraftFactory : DraftFactory<InfrastructureSurveyDraft> {
    override fun invoke(
        verificationObjectWIthCheckedSurvey: VerificationObjectWithCheckedSurvey
    ): InfrastructureSurveyDraft {
        return InfrastructureSurveyDraft(
            getSurvey(verificationObjectWIthCheckedSurvey.verificationObject.survey),
            getCheckedSurvey(verificationObjectWIthCheckedSurvey.checkedSurvey)
        )
    }

    private fun getCheckedSurvey(checkedSurvey: CheckedSurvey): InfrastructureCheckedSurvey {
        return when (checkedSurvey) {
            is CheckedSurvey.WastePlacementCheckedSurvey -> {
                checkedSurvey.infrastructureCheckedSurvey
            }
            is CheckedSurvey.WasteDisposalCheckedSurvey -> {
                checkedSurvey.infrastructureCheckedSurvey
            }
            is CheckedSurvey.WasteRecyclingCheckedSurvey -> {
                checkedSurvey.infrastructureCheckedSurvey
            }
            is CheckedSurvey.WasteTreatmentCheckedSurvey -> {
                checkedSurvey.infrastructureCheckedSurvey
            }
        }
    }

    private fun getSurvey(survey: Survey): InfrastructureSurvey {
        return when (survey) {
            is Survey.WasteDisposalSurvey -> {
                survey.infrastructureSurvey
            }
            is Survey.WastePlacementSurvey -> {
                survey.infrastructureSurvey
            }
            is Survey.WasteTreatmentSurvey -> {
                survey.infrastructureSurvey
            }
            is Survey.WasteRecyclingSurvey -> {
                survey.infrastructureSurvey
            }
        }
    }
}
