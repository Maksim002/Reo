package ru.ktsstudio.app_verification.domain.object_survey.schedule

import ru.ktsstudio.app_verification.domain.object_survey.common.DraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.ScheduleSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
class ScheduleDraftFactory : DraftFactory<ScheduleSurveyDraft> {
    override fun invoke(verificationObjectWithCheckedSurvey: VerificationObjectWithCheckedSurvey): ScheduleSurveyDraft {
        return ScheduleSurveyDraft(
            scheduleSurvey = verificationObjectWithCheckedSurvey.verificationObject.scheduleSurvey,
            scheduleCheckedSurvey = verificationObjectWithCheckedSurvey.checkedSurvey.workScheduleCheckedSurvey
        )
    }
}
