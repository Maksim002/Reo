package ru.ktsstudio.core_data_verfication_api.data.model.schedule

import arrow.optics.optics

@optics
data class TimeRange(
    val from: String,
    val to: String
) {
    companion object {
        const val EMPTY_TIME = "00:00"
        val EMPTY_RANGE = TimeRange(EMPTY_TIME, EMPTY_TIME)
        val ALL_DAY = TimeRange("00:00", "23:59")
    }
}