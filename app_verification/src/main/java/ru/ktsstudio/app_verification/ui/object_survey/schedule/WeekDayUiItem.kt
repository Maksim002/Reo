package ru.ktsstudio.app_verification.ui.object_survey.schedule

import ru.ktsstudio.core_data_verfication_api.data.model.schedule.WeekDay

/**
 * Created by Igor Park on 09/02/2021.
 */
data class WeekDayUiItem(
    val day: WeekDay,
    val title: String,
    val isSelected: Boolean,
    val isFirst: Boolean,
    val isLast: Boolean
)
