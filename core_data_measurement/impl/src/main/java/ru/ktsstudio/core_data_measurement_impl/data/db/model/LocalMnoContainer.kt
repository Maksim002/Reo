package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = LocalMnoContainer.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalMno::class,
            parentColumns = [LocalMno.COLUMN_ID],
            childColumns = [LocalMnoContainer.COLUMN_MNO_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalContainerType::class,
            parentColumns = [LocalContainerType.COLUMN_ID],
            childColumns = [LocalMnoContainer.COLUMN_TYPE_ID],
            deferred = true
        )
    ],
    indices = [
        Index(value = [LocalMnoContainer.COLUMN_MNO_ID]),
        Index(value = [LocalMnoContainer.COLUMN_TYPE_ID])
    ]
)
data class LocalMnoContainer(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_MNO_ID)
    val mnoId: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_TYPE_ID)
    val typeId: String,
    @ColumnInfo(name = COLUMN_VOLUME)
    val volume: Float,
    @ColumnInfo(name = COLUMN_TYPE_SCHEDULE)
    val scheduleType: String
) {
    companion object {
        const val TABLE_NAME = "container"

        const val COLUMN_ID = "containerId"
        const val COLUMN_MNO_ID = "mnoId"
        const val COLUMN_TYPE_ID = "type_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_VOLUME = "volume"
        private const val COLUMN_TYPE_SCHEDULE = "scheduleType"
    }
}