package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.ktsstudio.common.data.models.DraftState

@Entity(
    tableName = LocalMixedWasteContainer.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalMnoContainer::class,
            parentColumns = [LocalMnoContainer.COLUMN_ID],
            childColumns = [LocalMixedWasteContainer.COLUMN_MNO_CONTAINER_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalContainerType::class,
            parentColumns = [LocalContainerType.COLUMN_ID],
            childColumns = [LocalMixedWasteContainer.COLUMN_CONTAINER_TYPE_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalMeasurement::class,
            parentColumns = [LocalMeasurement.COLUMN_LOCAL_ID],
            childColumns = [LocalMixedWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID],
            deferred = true
        )
    ],
    indices = [
        Index(value = [LocalMixedWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID]),
        Index(value = [LocalMixedWasteContainer.COLUMN_CONTAINER_TYPE_ID]),
        Index(value = [LocalMixedWasteContainer.COLUMN_MNO_CONTAINER_ID])
    ]
)
data class LocalMixedWasteContainer(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_LOCAL_ID)
    val localId: Long = 0,
    @ColumnInfo(name = COLUMN_MEASUREMENT_LOCAL_ID)
    val measurementLocalId: Long,
    @ColumnInfo(name = COLUMN_IS_UNIQUE)
    val isUnique: Boolean,
    @ColumnInfo(name = COLUMN_MNO_CONTAINER_ID)
    val mnoContainerId: String?,
    @ColumnInfo(name = COLUMN_MNO_UNIQUE_CONTAINER_ID)
    val mnoUniqueContainerId: String?,
    @ColumnInfo(name = COLUMN_CONTAINER_TYPE_ID)
    val containerTypeId: String,
    @ColumnInfo(name = COLUMN_CONTAINER_NAME)
    val containerName: String?,
    @ColumnInfo(name = COLUMN_CONTAINER_FULLNESS)
    val containerFullness: Float?,
    @ColumnInfo(name = COLUMN_CONTAINER_VOLUME)
    val containerVolume: Float?,
    @ColumnInfo(name = COLUMN_WASTE_VOLUME)
    val wasteVolume: Float,
    @ColumnInfo(name = COLUMN_DAILY_GAIN_VOLUME)
    val dailyGainVolume: Float,
    @ColumnInfo(name = COLUMN_NET_WEIGHT)
    val netWeight: Float,
    @ColumnInfo(name = COLUMN_DAILY_GAIN_NET_WEIGHT)
    val dailyGainNetWeight: Float,
    @ColumnInfo(name = COLUMN_EMPTY_WEIGHT)
    val emptyContainerWeight: Float?,
    @ColumnInfo(name = COLUMN_FILLED_WEIGHT)
    val filledContainerWeight: Float?,
    @ColumnInfo(name = COLUMN_DRAFT_STATE)
    val draftState: DraftState
) {

    companion object {
        const val TABLE_NAME = "mixedWasteContainer"

        const val COLUMN_LOCAL_ID = "localId"
        const val COLUMN_MEASUREMENT_LOCAL_ID = "measurementLocalId"
        const val COLUMN_IS_UNIQUE = "isUnique"
        const val COLUMN_MNO_CONTAINER_ID = "mnoContainerId"
        const val COLUMN_MNO_UNIQUE_CONTAINER_ID = "uniqueMnoContainerId"
        const val COLUMN_CONTAINER_NAME = "name"
        const val COLUMN_CONTAINER_TYPE_ID = "typeId"
        const val COLUMN_CONTAINER_FULLNESS = "containerFullness"
        const val COLUMN_WASTE_VOLUME = "wasteVolume"
        const val COLUMN_CONTAINER_VOLUME = "containerVolume"
        const val COLUMN_DAILY_GAIN_VOLUME = "dailyGainVolume"
        const val COLUMN_NET_WEIGHT = "netWeight"
        const val COLUMN_DAILY_GAIN_NET_WEIGHT = "dailyGainNetWeight"
        const val COLUMN_EMPTY_WEIGHT = "emptyWeight"
        const val COLUMN_FILLED_WEIGHT = "filledWeight"
        const val COLUMN_DRAFT_STATE = "draftState"
    }
}