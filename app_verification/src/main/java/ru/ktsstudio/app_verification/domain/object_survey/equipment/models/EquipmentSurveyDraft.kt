package ru.ktsstudio.app_verification.domain.object_survey.equipment.models

import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
@optics
data class EquipmentSurveyDraft(
    val equipment: Survey.WasteTreatmentSurvey.EquipmentSurvey,
    val checkedEquipment: CheckedSurvey.WasteTreatmentCheckedSurvey.EquipmentCheckedSurvey
) {
    companion object
}
