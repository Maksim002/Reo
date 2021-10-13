package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class LocalWasteGroupWithRelations(
    @Embedded
    val wasteGroup: LocalWasteGroup,
    @Relation(
        entity = LocalWasteSubgroup::class,
        parentColumn = LocalWasteGroup.COLUMN_ID,
        entityColumn = LocalWasteSubgroup.COLUMN_GROUP_ID
    )
    val wasteSubgroups: List<LocalWasteSubgroup>
)