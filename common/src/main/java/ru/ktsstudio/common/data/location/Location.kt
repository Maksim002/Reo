package ru.ktsstudio.common.data.location

sealed class Location {
    object Unavailable : Location() {
        override fun toString() = "Unavailable"
    }
    data class Available(
        val latitude: Double,
        val longitude: Double
    ) : Location()
}