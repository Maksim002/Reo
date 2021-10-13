package ru.ktsstudio.common.utils.db.converters

import androidx.room.TypeConverter
import org.threeten.bp.Instant

class InstantDateConverter {
    @TypeConverter
    fun toLong(instant: Instant?) = instant?.toEpochMilli()

    @TypeConverter
    fun fromLong(millis: Long?): Instant? = millis?.let(Instant::ofEpochMilli)
}