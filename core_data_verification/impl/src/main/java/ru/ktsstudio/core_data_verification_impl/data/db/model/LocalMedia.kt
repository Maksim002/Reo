package ru.ktsstudio.core_data_verification_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint

/**
 * @author Maxim Ovchinnikov on 15.12.2020.
 */
@Entity(
    tableName = LocalMedia.TABLE_NAME,
    indices = [
        Index(
            value = [LocalMedia.COLUMN_FILE_PATH],
            unique = true
        )
    ]
)
data class LocalMedia(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = COLUMN_REMOTE_ID)
    val remoteId: String?,
    @ColumnInfo(name = COLUMN_FILE_PATH)
    val localFilePath: String?,
    @ColumnInfo(name = COLUMN_GPS)
    val gpsPoint: GpsPoint,
    @ColumnInfo(name = COLUMN_DATE)
    val date: Instant
) {
    companion object {
        const val TABLE_NAME = "media"

        const val COLUMN_ID = "id"
        const val COLUMN_REMOTE_ID = "remoteId"
        const val COLUMN_FILE_PATH = "localFilePath"
        private const val COLUMN_GPS = "gpsPoint"
        private const val COLUMN_DATE = "date"
    }
}
