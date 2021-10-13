package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.ktsstudio.common.data.models.GpsPoint

@Entity(
    tableName = LocalMno.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalCategory::class,
            parentColumns = [LocalCategory.COLUMN_ID],
            childColumns = [LocalMno.COLUMN_CATEGORY_ID]
        )],
    indices = [Index(LocalMno.COLUMN_CATEGORY_ID)]
)
data class LocalMno(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_TASK_IDS)
    val taskIds: List<String>,
    @ColumnInfo(name = COLUMN_LOCATION)
    val location: GpsPoint,
    @ColumnInfo(name = COLUMN_NAME_SOURCE)
    val nameSource: String,
    @ColumnInfo(name = COLUMN_TYPE_SOURCE)
    val typeSource: String,
    @ColumnInfo(name = COLUMN_CATEGORY_ID)
    val categoryId: String,
    @ColumnInfo(name = COLUMN_SUBCATEGORY)
    val subcategory: String,
    @ColumnInfo(name = COLUMN_TYPE_UNIT)
    val typeUnit: String,
    @ColumnInfo(name = COLUMN_QUANTITY_UNIT)
    val quantityUnit: Double,
    @ColumnInfo(name = COLUMN_TYPE_UNIT_ALT)
    val typeUnitAlt: String,
    @ColumnInfo(name = COLUMN_QUANTITY_UNIT_ALT)
    val quantityUnitAlt: Double,
    @ColumnInfo(name = COLUMN_FEDERAL_DISTRICT)
    val federalDistrict: String?,
    @ColumnInfo(name = COLUMN_REGION)
    val region: String?,
    @ColumnInfo(name = COLUMN_MUNICIPAL_DISTRICT)
    val municipalDistrict: String?,
    @ColumnInfo(name = COLUMN_ADDRESS)
    val address: String
) {

    companion object {
        const val TABLE_NAME = "mno"

        const val COLUMN_ID = "mnoId"
        private const val COLUMN_TASK_IDS = "taskIds"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_NAME_SOURCE = "nameSource"
        private const val COLUMN_TYPE_SOURCE = "typeSource"
        const val COLUMN_CATEGORY_ID = "categoryId"
        private const val COLUMN_SUBCATEGORY = "subcategory"
        private const val COLUMN_TYPE_UNIT = "typeUnit"
        private const val COLUMN_QUANTITY_UNIT = "quantityUnit"
        private const val COLUMN_TYPE_UNIT_ALT = "typeUnitAlt"
        private const val COLUMN_QUANTITY_UNIT_ALT = "quantityUnitAlt"
        private const val COLUMN_FEDERAL_DISTRICT = "federalDistrict"
        private const val COLUMN_REGION = "region"
        private const val COLUMN_MUNICIPAL_DISTRICT = "municipalDistrict"
    }
}