package ru.ktsstudio.common.data.models

/**
 * Created by Igor Park on 02/10/2020.
 */
data class GpsPoint(
    val lat: Double,
    val lng: Double
) {
    fun isEmpty(): Boolean = this == EMPTY

    companion object {
        val EMPTY = GpsPoint(0.0, 0.0)
    }
}