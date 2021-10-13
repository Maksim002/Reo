package ru.ktsstudio.core_data_verfication_api.data.model.schedule

import arrow.optics.optics

@optics
data class ScheduleTime(
    val workTime: TimeRange,
    val breakTime: TimeRange
) {
    companion object {
        val DEFAULT = ScheduleTime(TimeRange.EMPTY_RANGE, TimeRange.EMPTY_RANGE)
    }
}