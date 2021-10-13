package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.LocalModelState

@Entity(tableName = LocalMedia.TABLE_NAME)
data class LocalMedia(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_LOCAL_ID)
    val localId: Long = 0,
    @ColumnInfo(name = COLUMN_REMOTE_ID)
    val remoteId: String,
    @ColumnInfo(name = COLUMN_URL)
    val url: String?,
    @ColumnInfo(name = COLUMN_CACHED_FILE)
    val cachedFilePath: String?,
    @ColumnInfo(name = COLUMN_GPS_POINT)
    val gpsPoint: GpsPoint?,
    @ColumnInfo(name = COLUMN_DATE)
    val date: Instant?,
    @ColumnInfo(name = COLUMN_MEDIA_TYPE)
    val mediaType: MediaType,
    @ColumnInfo(name = COLUMN_DRAFT_STATE)
    val draftState: DraftState,
    @ColumnInfo(name = COLUMN_LOCAL_STATE)
    val localState: LocalModelState
) {

    companion object {
        const val TABLE_NAME = "media"

        const val COLUMN_LOCAL_ID = "localId"
        const val COLUMN_REMOTE_ID = "remoteId"
        const val COLUMN_URL = "url"
        const val COLUMN_CACHED_FILE = "cached_file"
        const val COLUMN_GPS_POINT = "gps"
        const val COLUMN_MEDIA_TYPE = "mediaType"
        const val COLUMN_DRAFT_STATE = "draftState"
        const val COLUMN_LOCAL_STATE = "localState"
        const val COLUMN_DATE = "date"
    }
}
