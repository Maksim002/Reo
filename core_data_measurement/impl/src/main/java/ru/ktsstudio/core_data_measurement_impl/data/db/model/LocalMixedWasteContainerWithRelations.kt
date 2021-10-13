package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */

data class LocalMixedWasteContainerWithRelations(
    @Embedded
    val container: LocalMixedWasteContainer,

    @Relation(
        entity = LocalContainerType::class,
        parentColumn = LocalMixedWasteContainer.COLUMN_CONTAINER_TYPE_ID,
        entityColumn = LocalContainerType.COLUMN_ID
    )
    val containerType: LocalContainerType,

    @Relation(
        entity = LocalMnoContainer::class,
        parentColumn = LocalMixedWasteContainer.COLUMN_MNO_CONTAINER_ID,
        entityColumn = LocalMnoContainer.COLUMN_ID
    )
    val mnoContainer: LocalMnoContainerWithRelations?
)