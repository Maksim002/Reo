package ru.ktsstudio.app_verification.domain.object_survey.schedule.models

import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.WorkScheduleCheckedSurvey

/**
 * @author Maxim Ovchinnikov on 01.12.2020.
 */
@optics
data class ScheduleSurveyDraft(
    val scheduleSurvey: ScheduleSurvey,
    val scheduleCheckedSurvey: WorkScheduleCheckedSurvey
) {
    companion object
}
