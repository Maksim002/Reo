package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalMeasurementStatus.TABLE_NAME)
data class LocalMeasurementStatus(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_ORDER)
    val order: Int
) {
    companion object {
        const val TABLE_NAME = "measurementStatus"

        const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_ORDER = "order"
    }
}
