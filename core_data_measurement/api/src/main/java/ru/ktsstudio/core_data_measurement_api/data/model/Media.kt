package ru.ktsstudio.core_data_measurement_api.data.model

import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint
import java.io.File

data class Photo(
    val id: String,
    val url: String?,
    val cachedFile: File?,
    val gpsPoint: GpsPoint?,
    val date: Instant?
)

data class Video(
    val id: String,
    val url: String?,
    val cachedFile: File?,
    val gpsPoint: GpsPoint?,
    val date: Instant?
)