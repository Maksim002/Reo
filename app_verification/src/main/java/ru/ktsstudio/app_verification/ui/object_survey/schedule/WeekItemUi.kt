package ru.ktsstudio.app_verification.ui.object_survey.schedule

import ru.ktsstudio.app_verification.domain.object_survey.schedule.models.ScheduleSurveyDraft
import ru.ktsstudio.app_verification.ui.common.ValueConsumer

/**
 * Created by Igor Park on 09/02/2021.
 */
data class WeekItemUi(
    val workingDays: List<WeekDayUiItem>,
    val valueConsumer: ValueConsumer<String?, ScheduleSurveyDraft>
)
