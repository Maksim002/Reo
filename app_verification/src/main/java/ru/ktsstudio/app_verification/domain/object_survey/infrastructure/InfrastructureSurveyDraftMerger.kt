package ru.ktsstudio.app_verification.domain.object_survey.infrastructure

import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.VerificationObjectDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
class InfrastructureSurveyDraftMerger : VerificationObjectDraftMerger<InfrastructureSurveyDraft> {
    override fun invoke(
        verificationObjectWithSurvey: VerificationObjectWithCheckedSurvey,
        draft: InfrastructureSurveyDraft
    ): VerificationObjectWithCheckedSurvey {
        return verificationObjectWithSurvey.copy(
            verificationObject = verificationObjectWithSurvey.verificationObject.copy(
                survey = updateSurvey(
                    verificationObjectWithSurvey.verificationObject.survey,
                    draft.infrastructureSurvey
                )
            ),
            checkedSurvey = updateCheckedSurvey(
                verificationObjectWithSurvey.checkedSurvey,
                draft.infrastructureCheckedSurvey
            )
        )
    }

    private fun updateSurvey(
        survey: Survey,
        update: InfrastructureSurvey
    ): Survey {

        val surveyToSave = filterOutEmptyObject(update)
        return when (survey) {
            is Survey.WasteDisposalSurvey -> {
                survey.copy(infrastructureSurvey = surveyToSave)
            }
            is Survey.WastePlacementSurvey -> {
                survey.copy(infrastructureSurvey = surveyToSave)
            }
            is Survey.WasteTreatmentSurvey -> {
                survey.copy(infrastructureSurvey = surveyToSave)
            }
            is Survey.WasteRecyclingSurvey -> {
                survey.copy(infrastructureSurvey = surveyToSave)
            }
        }
    }

    private fun updateCheckedSurvey(
        checkedSurvey: CheckedSurvey,
        update: InfrastructureCheckedSurvey
    ): CheckedSurvey {
        return when (checkedSurvey) {
            is CheckedSurvey.WastePlacementCheckedSurvey -> {
                checkedSurvey.copy(infrastructureCheckedSurvey = update)
            }
            is CheckedSurvey.WasteDisposalCheckedSurvey -> {
                checkedSurvey.copy(infrastructureCheckedSurvey = update)
            }
            is CheckedSurvey.WasteRecyclingCheckedSurvey -> {
                checkedSurvey.copy(infrastructureCheckedSurvey = update)
            }
            is CheckedSurvey.WasteTreatmentCheckedSurvey -> {
                checkedSurvey.copy(infrastructureCheckedSurvey = update)
            }
        }
    }

    private fun filterOutEmptyObject(survey: InfrastructureSurvey): InfrastructureSurvey {
        return with(survey) {
            copy(
                weightControl = weightControl.copy(equipment = weightControl.equipment.filterValues { it.isFilled }.k())
                    .takeIf { it.availabilityInfo.isAvailable.not() }
                    ?: weightControl,
                wheelsWashing = wheelsWashing.copy(equipment = wheelsWashing.equipment.filterValues { it.isFilled }.k())
                    .takeIf { it.availabilityInfo.isAvailable.not() }
                    ?: wheelsWashing,
                sewagePlant = sewagePlant
                    .copy(equipment = sewagePlant.equipment.filterValues { it.isFilled }.k())
                    .takeIf { it.availabilityInfo.isAvailable.not() }
                    ?: sewagePlant,
                radiationControl = radiationControl
                    .copy(equipment = radiationControl.equipment.filterValues { it.isFilled }.k())
                    .takeIf { it.availabilityInfo.isAvailable.not() }
                    ?: radiationControl
            )
        }
    }
}
