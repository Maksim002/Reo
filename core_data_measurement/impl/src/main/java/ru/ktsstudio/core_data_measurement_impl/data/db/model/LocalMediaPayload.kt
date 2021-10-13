package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint

data class LocalMediaPayload(
    @ColumnInfo(name = LocalMedia.COLUMN_LOCAL_ID)
    val localId: Long,
    @ColumnInfo(name = LocalMedia.COLUMN_GPS_POINT)
    val gpsPoint: GpsPoint?,
    @ColumnInfo(name = LocalMedia.COLUMN_CACHED_FILE)
    val cachedFilePath: String,
    @ColumnInfo(name = LocalMedia.COLUMN_DATE)
    val date: Instant
)