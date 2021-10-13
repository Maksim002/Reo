package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

data class LocalMorphologyWithRelation(
    @Embedded
    val morphology: LocalMorphology,
    @Relation(
        entity = LocalWasteGroup::class,
        parentColumn = LocalMorphology.COLUMN_GROUP_ID,
        entityColumn = LocalWasteGroup.COLUMN_ID
    )
    val wasteGroup: LocalWasteGroup,
    @Relation(
        entity = LocalWasteSubgroup::class,
        parentColumn = LocalMorphology.COLUMN_SUBGROUP_ID,
        entityColumn = LocalWasteSubgroup.COLUMN_ID
    )
    val wasteSubgroup: LocalWasteSubgroup?
)