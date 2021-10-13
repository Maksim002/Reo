package ru.ktsstudio.app_verification.domain.object_survey.equipment

import ru.ktsstudio.app_verification.domain.object_survey.common.DraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
class EquipmentDraftFactory : DraftFactory<EquipmentSurveyDraft> {
    override fun invoke(
        verificationObjectWithCheckedSurvey: VerificationObjectWithCheckedSurvey
    ): EquipmentSurveyDraft {
        return EquipmentSurveyDraft(
            equipment = verificationObjectWithCheckedSurvey.verificationObject
                .survey
                .let { it as? Survey.WasteTreatmentSurvey }
                ?.equipmentSurvey
                ?: error("equipment is available only in WasteTreatmentSurvey"),
            checkedEquipment = verificationObjectWithCheckedSurvey
                .checkedSurvey
                .let { it as? CheckedSurvey.WasteTreatmentCheckedSurvey }
                ?.equipmentCheckedSurvey
                ?: error("equipment is available only in WasteTreatmentSurvey"),
        )
    }
}
