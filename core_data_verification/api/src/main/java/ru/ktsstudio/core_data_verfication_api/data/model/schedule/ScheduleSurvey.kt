package ru.ktsstudio.core_data_verfication_api.data.model.schedule

import arrow.optics.optics

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.11.2020.
 */
@optics
data class ScheduleSurvey(
    val schedule: Schedule?,
    val shiftsPerDayCount: Int?,
    val workDaysPerYearCount: Int?,
    val workplacesCount: Int?,
    val managersCount: Int?,
    val workersCount: Int?
) {
    companion object
}