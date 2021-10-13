package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class LocalContainerWasteTypeWithRelation(
    @Embedded
    val wasteType: LocalContainerWasteType,

    @Relation(
        entity = LocalWasteCategory::class,
        parentColumn = LocalContainerWasteType.COLUMN_CATEGORY_ID,
        entityColumn = LocalWasteCategory.COLUMN_ID
    )
    val category: LocalWasteCategory
)