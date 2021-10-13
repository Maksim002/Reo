package ru.ktsstudio.app_verification.domain.object_survey.infrastructure

import ru.ktsstudio.app_verification.domain.object_survey.common.SurveyDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.utilities.extensions.orTrue

/**
 * Created by Igor Park on 13/12/2020.
 */
class InfrastructureDraftValidator : SurveyDraftValidator<InfrastructureSurveyDraft> {
    override fun invoke(draft: InfrastructureSurveyDraft): Boolean {
        val survey = draft.infrastructureSurvey
        return with(draft.infrastructureCheckedSurvey) {
            (weightControl.not() || survey.weightControl.checkValidity()) &&
                (wheelsWashing.not() || survey.wheelsWashing.checkValidity()) &&
                (sewagePlant.not() || survey.sewagePlant.checkValidity()) &&
                (radiationControl.not() || survey.radiationControl.checkValidity()) &&
                (securityCamera.not() || survey.securityCamera.checkValidity()) &&
                (roadNetwork.not() || survey.roadNetwork.checkValidity()) &&
                (fences.not() || survey.fences.checkValidity()) &&
                (lightSystem.not() || survey.lightSystem.checkValidity()) &&
                (securityStation.not() || survey.securityStation.checkValidity()) &&
                (asu.not() || survey.asu.checkValidity()) &&
                environmentMonitoring?.let { environmentMonitoring ->
                    environmentMonitoring.not() || survey.environmentMonitoring!!.checkValidity()
                }.orTrue()
        }
    }
}
