package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LocalContainerType.TABLE_NAME)
data class LocalContainerType(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_IS_SEPARATE)
    val isSeparate: Boolean
) {
    companion object {
        const val TABLE_NAME = "containerType"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_IS_SEPARATE = "is_separate"
    }
}