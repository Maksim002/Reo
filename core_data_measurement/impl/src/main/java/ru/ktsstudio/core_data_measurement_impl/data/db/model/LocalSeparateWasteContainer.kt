package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.ktsstudio.common.data.models.DraftState

@Entity(
    tableName = LocalSeparateWasteContainer.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = LocalMeasurement::class,
            parentColumns = [LocalMeasurement.COLUMN_LOCAL_ID],
            childColumns = [LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalContainerType::class,
            parentColumns = [LocalContainerType.COLUMN_ID],
            childColumns = [LocalSeparateWasteContainer.COLUMN_CONTAINER_TYPE_ID],
            deferred = true
        ),
        ForeignKey(
            entity = LocalMnoContainer::class,
            parentColumns = [LocalMnoContainer.COLUMN_ID],
            childColumns = [LocalSeparateWasteContainer.COLUMN_MNO_CONTAINER_ID],
            deferred = true
        )
    ],
    indices = [
        Index(value = [LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID]),
        Index(value = [LocalSeparateWasteContainer.COLUMN_CONTAINER_TYPE_ID]),
        Index(value = [LocalSeparateWasteContainer.COLUMN_MNO_CONTAINER_ID])
    ]
)
data class LocalSeparateWasteContainer(
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
    @ColumnInfo(name = COLUMN_CONTAINER_NAME)
    val containerName: String?,
    @ColumnInfo(name = COLUMN_CONTAINER_VOLUME)
    val containerVolume: Float?,
    @ColumnInfo(name = COLUMN_CONTAINER_TYPE_ID)
    val containerTypeId: String,
    @ColumnInfo(name = COLUMN_DRAFT_STATE)
    val draftState: DraftState
) {

    companion object {
        const val TABLE_NAME = "separateWasteContainer"

        const val COLUMN_LOCAL_ID = "localId"
        const val COLUMN_MEASUREMENT_LOCAL_ID = "measurementLocalId"
        const val COLUMN_IS_UNIQUE = "isUnique"
        const val COLUMN_MNO_CONTAINER_ID = "containerMnoId"
        const val COLUMN_MNO_UNIQUE_CONTAINER_ID = "uniqueMnoContainerId"
        const val COLUMN_CONTAINER_NAME = "name"
        const val COLUMN_CONTAINER_VOLUME = "containerVolume"
        const val COLUMN_CONTAINER_TYPE_ID = "containerTypeId"
        const val COLUMN_DRAFT_STATE = "draftState"
    }
}