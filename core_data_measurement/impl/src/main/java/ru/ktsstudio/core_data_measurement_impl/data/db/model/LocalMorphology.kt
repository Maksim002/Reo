package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.ktsstudio.common.data.models.DraftState

@Entity(
    tableName = LocalMorphology.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalMeasurement::class,
            parentColumns = [LocalMeasurement.COLUMN_LOCAL_ID],
            childColumns = [LocalMorphology.COLUMN_MEASUREMENT_LOCAL_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalWasteGroup::class,
            parentColumns = [LocalWasteGroup.COLUMN_ID],
            childColumns = [LocalMorphology.COLUMN_GROUP_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalWasteSubgroup::class,
            parentColumns = [LocalWasteSubgroup.COLUMN_ID],
            childColumns = [LocalMorphology.COLUMN_SUBGROUP_ID],
            deferred = true
        )
    ],
    indices = [
        Index(value = [LocalMorphology.COLUMN_MEASUREMENT_LOCAL_ID]),
        Index(value = [LocalMorphology.COLUMN_GROUP_ID]),
        Index(value = [LocalMorphology.COLUMN_SUBGROUP_ID])
    ]
)
data class LocalMorphology(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_LOCAL_ID)
    val localId: Long = 0,
    @ColumnInfo(name = COLUMN_GROUP_ID)
    val groupId: String,
    @ColumnInfo(name = COLUMN_SUBGROUP_ID)
    val subgroupId: String?,
    @ColumnInfo(name = COLUMN_MEASUREMENT_LOCAL_ID)
    val measurementId: Long,
    @ColumnInfo(name = COLUMN_DAILY_GAIN_VOLUME)
    val dailyGainVolume: Float,
    @ColumnInfo(name = COLUMN_DAILY_GAIN_WEIGHT)
    val dailyGainWeight: Float,
    @ColumnInfo(name = COLUMN_DRAFT_STATE)
    val draftState: DraftState

) {
    companion object {
        const val TABLE_NAME = "morphologies"

        const val COLUMN_LOCAL_ID = "localId"
        const val COLUMN_GROUP_ID = "groupId"
        const val COLUMN_MEASUREMENT_LOCAL_ID = "measurementId"
        const val COLUMN_SUBGROUP_ID = "subgroupId"
        const val COLUMN_DAILY_GAIN_VOLUME = "dailyGainVolume"
        const val COLUMN_DAILY_GAIN_WEIGHT = "dailyGainWeight"
        const val COLUMN_DRAFT_STATE = "draftState"
    }
}