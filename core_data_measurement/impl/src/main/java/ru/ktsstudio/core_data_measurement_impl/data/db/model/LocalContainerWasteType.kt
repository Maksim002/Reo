package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.ktsstudio.common.data.models.DraftState

@Entity(
    tableName = LocalContainerWasteType.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalSeparateWasteContainer::class,
            parentColumns = [LocalSeparateWasteContainer.COLUMN_LOCAL_ID],
            childColumns = [LocalContainerWasteType.COLUMN_CONTAINER_LOCAL_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalWasteCategory::class,
            parentColumns = [LocalWasteCategory.COLUMN_ID],
            childColumns = [LocalContainerWasteType.COLUMN_CATEGORY_ID],
            deferred = true
        )
    ],
    indices = [
        Index(value = [LocalContainerWasteType.COLUMN_CONTAINER_LOCAL_ID]),
        Index(value = [LocalContainerWasteType.COLUMN_CATEGORY_ID])
    ]
)
data class LocalContainerWasteType(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_LOCAL_ID)
    val localId: String,
    @ColumnInfo(name = COLUMN_CATEGORY_ID)
    val categoryId: String,
    @ColumnInfo(name = COLUMN_CONTAINER_LOCAL_ID)
    val containerLocalId: Long,
    @ColumnInfo(name = COLUMN_CATEGORY_NAME)
    val categoryName: String,
    @ColumnInfo(name = COLUMN_NAME_OTHER_CATEGORY)
    val nameOtherCategory: String?,
    @ColumnInfo(name = COLUMN_CONTAINER_VOLUME)
    val containerVolume: Float?,
    @ColumnInfo(name = COLUMN_CONTAINER_FULLNESS)
    val containerFullness: Float?,
    @ColumnInfo(name = COLUMN_WASTE_VOLUME)
    val wasteVolume: Float,
    @ColumnInfo(name = COLUMN_DAILY_GAIN_VOLUME)
    val dailyGainVolume: Float,
    @ColumnInfo(name = COLUMN_NET_WEIGHT)
    val netWeight: Float,
    @ColumnInfo(name = COLUMN_DAILY_GAIN_NET_WEIGHT)
    val dailyGainNetWeight: Float,
    @ColumnInfo(name = COLUMN_DRAFT_STATE)
    val draftState: DraftState
) {

    companion object {
        const val TABLE_NAME = "containerWasteType"

        const val COLUMN_LOCAL_ID = "localId"
        const val COLUMN_CATEGORY_ID = "categoryId"
        const val COLUMN_CONTAINER_LOCAL_ID = "containerLocalId"
        const val COLUMN_CATEGORY_NAME = "categoryName"
        const val COLUMN_NAME_OTHER_CATEGORY = "nameOtherCategory"
        const val COLUMN_CONTAINER_VOLUME = "containerVolume"
        const val COLUMN_CONTAINER_FULLNESS = "containerFullness"
        const val COLUMN_WASTE_VOLUME = "wasteVolume"
        const val COLUMN_DAILY_GAIN_VOLUME = "dailyGainVolume"
        const val COLUMN_NET_WEIGHT = "netWeight"
        const val COLUMN_DAILY_GAIN_NET_WEIGHT = "dailyGainNetWeight"
        const val COLUMN_DRAFT_STATE = "draftState"
    }
}