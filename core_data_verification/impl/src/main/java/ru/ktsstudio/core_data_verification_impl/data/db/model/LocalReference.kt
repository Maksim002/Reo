package ru.ktsstudio.core_data_verification_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType

/**
 * Created by Igor Park on 11/12/2020.
 */
@Entity(tableName = LocalReference.TABLE_NAME)
data class LocalReference(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_REFERENCE_TYPE)
    val type: ReferenceType
) {
    companion object {
        const val TABLE_NAME = "referenceObjects"

        const val COLUMN_ID = "id"
        const val COLUMN_REFERENCE_TYPE = "referenceType"
        private const val COLUMN_NAME = "name"
    }
}