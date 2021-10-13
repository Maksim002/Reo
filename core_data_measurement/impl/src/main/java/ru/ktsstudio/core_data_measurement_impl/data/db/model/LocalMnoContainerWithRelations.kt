package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Igor Park on 28/10/2020.
 */
data class LocalMnoContainerWithRelations(
    @Embedded
    val container: LocalMnoContainer,
    @Relation(
        entity = LocalContainerType::class,
        parentColumn = LocalMnoContainer.COLUMN_TYPE_ID,
        entityColumn = LocalContainerType.COLUMN_ID
    )
    val containerType: LocalContainerType
)