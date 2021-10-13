package ru.ktsstudio.app_verification.domain.object_inspection

import ru.ktsstudio.core_data_verfication_api.data.model.Progress

/**
 * @author Maxim Ovchinnikov on 21.11.2020.
 */
data class ObjectInspectionProgress(
    val menuInfoProgress: Progress,
    val scheduleProgress: Progress,
    val techProgress: Progress,
    val infrastructureProgress: Progress
) {
    fun isInspectionDone(): Boolean {
        return menuInfoProgress.isDone() &&
            scheduleProgress.isDone() &&
            techProgress.isDone() &&
            infrastructureProgress.isDone()
    }
}
