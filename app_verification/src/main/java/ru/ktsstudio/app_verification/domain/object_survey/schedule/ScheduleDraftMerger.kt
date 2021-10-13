package ru.ktsstudio.app_verification.domain.object_survey.schedule

import ru.ktsstudio.app_verification.domain.object_survey.common.VerificationObjectDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.ScheduleSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 10.12.2020.
 */
class ScheduleDraftMerger : VerificationObjectDraftMerger<ScheduleSurveyDraft> {
    override fun invoke(
        verificationObjectWithSurvey: VerificationObjectWithCheckedSurvey,
        draft: ScheduleSurveyDraft
    ): VerificationObjectWithCheckedSurvey {
        return verificationObjectWithSurvey.copy(
            verificationObject = verificationObjectWithSurvey.verificationObject.copy(
                scheduleSurvey = draft.scheduleSurvey
            ),
            checkedSurvey = verificationObjectWithSurvey.checkedSurvey.copyWorkSchedule(draft.scheduleCheckedSurvey)
        )
    }
}
