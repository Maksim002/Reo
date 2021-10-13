package ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models

import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey

@optics
data class InfrastructureSurveyDraft(
    val infrastructureSurvey: InfrastructureSurvey,
    val infrastructureCheckedSurvey: InfrastructureCheckedSurvey
) {
    companion object
}
