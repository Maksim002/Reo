package ru.ktsstudio.app_verification.domain.object_survey.schedule

import ru.ktsstudio.app_verification.domain.object_survey.common.SurveyDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.ScheduleSurveyDraft
import ru.ktsstudio.utilities.extensions.orFalse

/**
 * Created by Igor Park on 11/02/2021.
 */
class ScheduleSurveyValidator : SurveyDraftValidator<ScheduleSurveyDraft> {
    override fun invoke(draft: ScheduleSurveyDraft): Boolean {
        val survey = draft.scheduleSurvey
        return with(draft.scheduleCheckedSurvey) {
            (schedule.not() || survey.schedule?.checkValidity().orFalse()) &&
                (shiftsPerDayCount.not() || survey.shiftsPerDayCount != null) &&
                (workDaysPerYearCount.not() || survey.workDaysPerYearCount != null) &&
                (workplacesCount.not() || survey.workplacesCount != null) &&
                (managersCount.not() || survey.managersCount != null) &&
                (workersCount.not() || survey.workersCount != null)
        }
    }
}
