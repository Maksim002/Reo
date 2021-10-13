package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */

data class LocalSeparateWasteContainerWithRelations(
    @Embedded
    val container: LocalSeparateWasteContainer,
    @Relation(
        entity = LocalContainerType::class,
        parentColumn = LocalSeparateWasteContainer.COLUMN_CONTAINER_TYPE_ID,
        entityColumn = LocalContainerType.COLUMN_ID
    )
    val containerType: LocalContainerType,

    @Relation(
        entity = LocalMnoContainer::class,
        parentColumn = LocalSeparateWasteContainer.COLUMN_MNO_CONTAINER_ID,
        entityColumn = LocalMnoContainer.COLUMN_ID
    )
    val mnoContainer: LocalMnoContainerWithRelations?,
    @Relation(
        entity = LocalContainerWasteType::class,
        parentColumn = LocalSeparateWasteContainer.COLUMN_LOCAL_ID,
        entityColumn = LocalContainerWasteType.COLUMN_CONTAINER_LOCAL_ID
    )
    val wasteTypes: List<LocalContainerWasteTypeWithRelation>
)