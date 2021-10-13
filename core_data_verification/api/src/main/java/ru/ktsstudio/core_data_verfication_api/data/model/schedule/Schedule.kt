package ru.ktsstudio.core_data_verfication_api.data.model.schedule

import arrow.optics.optics

/**
 * Created by Igor Park on 07/02/2021.
 */
@optics
data class Schedule(
    val isFullDay: Boolean,
    val withoutBreaks: Boolean,
    val workMode: WorkMode
) {
    fun checkValidity(): Boolean {
        return workMode.checkValidity()
    }

    companion object
}
