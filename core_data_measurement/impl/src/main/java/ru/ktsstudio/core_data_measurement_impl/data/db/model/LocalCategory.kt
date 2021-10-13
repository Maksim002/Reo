package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.10.2020.
 */

@Entity(tableName = LocalCategory.TABLE_NAME)
data class LocalCategory(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String
) {

    companion object {
        const val TABLE_NAME = "categories"

        const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
    }
}