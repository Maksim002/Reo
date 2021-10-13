package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.data.models.LocalModelState

@Entity(
    tableName = LocalMeasurement.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalMeasurementStatus::class,
            parentColumns = [LocalMeasurementStatus.COLUMN_ID],
            childColumns = [LocalMeasurement.COLUMN_MEASUREMENT_STATUS_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalMno::class,
            parentColumns = [LocalMno.COLUMN_ID],
            childColumns = [LocalMeasurement.COLUMN_MNO_ID],
            deferred = true
        )
    ],
    indices = [
        Index(
            value = [LocalMeasurement.COLUMN_MEASUREMENT_STATUS_ID]
        ),
        Index(
            value = [LocalMeasurement.COLUMN_MNO_ID]
        )
    ]
)
data class LocalMeasurement(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_LOCAL_ID)
    val localId: Long = 0,
    @ColumnInfo(name = COLUMN_REMOTE_ID)
    val remoteId: String?,
    @ColumnInfo(name = COLUMN_MNO_ID)
    val mnoId: String,
    @ColumnInfo(name = COLUMN_MEASUREMENT_STATUS_ID)
    val measurementStatusId: String,
    @ColumnInfo(name = COLUMN_GPS_POINT)
    val gpsPoint: GpsPoint?,
    @ColumnInfo(name = COLUMN_SEASON)
    val season: String,
    @ColumnInfo(name = COLUMN_DATE)
    val date: Instant,
    @ColumnInfo(name = COLUMN_IS_POSSIBLE)
    val isPossible: Boolean,
    @ColumnInfo(name = COLUMN_IMPOSSIBILITY_REASON)
    val impossibilityReason: String?,
    @ColumnInfo(name = COLUMN_COMMENT)
    val comment: String?,
    @ColumnInfo(name = COLUMN_REVISION_COMMENT)
    val revisionComment: String?,
    @ColumnInfo(name = COLUMN_LOCAL_STATE)
    val state: LocalModelState
) {

    companion object {
        const val TABLE_NAME = "measurement"

        const val COLUMN_LOCAL_ID = "localId"
        const val COLUMN_REMOTE_ID = "remoteId"
        const val COLUMN_MNO_ID = "mnoId"
        const val COLUMN_MEASUREMENT_STATUS_ID = "measurementStatusId"
        const val COLUMN_GPS_POINT = "gpsPoint"
        const val COLUMN_SEASON = "season"
        const val COLUMN_DATE = "date"
        const val COLUMN_IS_POSSIBLE = "isPossible"
        const val COLUMN_IMPOSSIBILITY_REASON = "impossibilityReason"
        const val COLUMN_COMMENT = "comment"
        const val COLUMN_LOCAL_STATE = "localState"
        const val COLUMN_REVISION_COMMENT = "revisionComment"
    }
}
