package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory

@Entity(
    tableName = LocalMeasurementMedia.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalMeasurement::class,
            parentColumns = [LocalMeasurement.COLUMN_LOCAL_ID],
            childColumns = [LocalMeasurementMedia.COLUMN_MEASUREMENT_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalMedia::class,
            parentColumns = [LocalMedia.COLUMN_LOCAL_ID],
            childColumns = [LocalMeasurementMedia.COLUMN_MEDIA_LOCAL_ID],
            deferred = true
        )
    ],
    indices = [
        Index(
            value = [LocalMeasurementMedia.COLUMN_MEASUREMENT_ID]
        ),
        Index(
            value = [LocalMeasurementMedia.COLUMN_MEDIA_LOCAL_ID]
        )
    ]
)
data class LocalMeasurementMedia(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,
    @ColumnInfo(name = COLUMN_MEASUREMENT_ID)
    val measurementId: Long,
    @ColumnInfo(name = COLUMN_MEDIA_LOCAL_ID)
    val mediaLocalId: Long,
    @ColumnInfo(name = COLUMN_MEDIA_CATEGORY)
    val mediaCategory: MeasurementMediaCategory
) {
    companion object {
        const val TABLE_NAME = "measurementMedia"

        const val COLUMN_ID = "id"
        const val COLUMN_MEASUREMENT_ID = "measurementId"
        const val COLUMN_MEDIA_LOCAL_ID = "mediaLocalId"
        const val COLUMN_MEDIA_CATEGORY = "category"
    }
}