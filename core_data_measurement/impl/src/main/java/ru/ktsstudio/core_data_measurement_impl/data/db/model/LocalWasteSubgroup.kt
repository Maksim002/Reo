package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = LocalWasteSubgroup.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalWasteGroup::class,
            parentColumns = [LocalWasteGroup.COLUMN_ID],
            childColumns = [LocalWasteSubgroup.COLUMN_GROUP_ID],
            deferred = true
        )
    ],
    indices = [
        Index(value = [LocalWasteSubgroup.COLUMN_GROUP_ID])
    ]
)
data class LocalWasteSubgroup(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_GROUP_ID)
    val groupId: String
) {
    companion object {
        const val TABLE_NAME = "wasteSubgroup"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_GROUP_ID = "groupId"
    }
}