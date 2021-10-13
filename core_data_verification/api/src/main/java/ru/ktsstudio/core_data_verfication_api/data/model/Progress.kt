package ru.ktsstudio.core_data_verfication_api.data.model

/**
 * @author Maxim Ovchinnikov on 20.11.2020.
 */
data class Progress(
    val current: Int,
    val max: Int
) {
    fun isDone() = current == max

    operator fun plus(other: Progress): Progress {
        return Progress(
            current = current + other.current,
            max = max + other.max
        )
    }
}