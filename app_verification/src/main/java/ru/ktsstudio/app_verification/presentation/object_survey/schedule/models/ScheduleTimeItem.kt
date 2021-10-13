package ru.ktsstudio.app_verification.presentation.object_survey.schedule.models

import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.ScheduleSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.WeekDay

/**
 * Created by Igor Park on 09/02/2021.
 */
data class ScheduleTimeItem(
    val day: WeekDay?,
    val displayName: String,
    val isFullDay: Boolean,
    val withoutBreaks: Boolean,
    val workStart: StringValueConsumer<ScheduleSurveyDraft>,
    val workEnd: StringValueConsumer<ScheduleSurveyDraft>,
    val breakStart: StringValueConsumer<ScheduleSurveyDraft>,
    val breakEnd: StringValueConsumer<ScheduleSurveyDraft>
)
