package ru.ktsstudio.common.utils.db.converters

import androidx.room.TypeConverter
import ru.ktsstudio.common.data.models.GpsPoint

class GpsPointRoomConverter {

    @TypeConverter
    fun fromLatLng(gpsPoint: GpsPoint?): String? = buildString {
        if (gpsPoint == null) return null
        append(gpsPoint.lat)
        append(DELIMITER)
        append(gpsPoint.lng)
    }

    @TypeConverter
    fun toLatLng(serialized: String?): GpsPoint? {
        if (serialized.isNullOrBlank()) return null
        val (lat, lng) = serialized.split(DELIMITER)
        return GpsPoint(
            lat.toDoubleOrNull() ?: return null,
            lng.toDoubleOrNull() ?: return null,
        )
    }

    companion object {
        private const val DELIMITER = '&'
    }
}