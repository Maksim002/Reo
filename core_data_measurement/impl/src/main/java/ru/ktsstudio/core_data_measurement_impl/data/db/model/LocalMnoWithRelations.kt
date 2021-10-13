package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @author Maxim Myalkin (MaxMyalkin) on 08.10.2020.
 */
data class LocalMnoWithRelations(
    @Embedded
    val mno: LocalMno,
    @Relation(
        entity = LocalMnoContainer::class,
        parentColumn = LocalMno.COLUMN_ID,
        entityColumn = LocalMnoContainer.COLUMN_MNO_ID
    )
    val containers: List<LocalMnoContainerWithRelations>,

    @Relation(
        entity = LocalCategory::class,
        parentColumn = LocalMno.COLUMN_CATEGORY_ID,
        entityColumn = LocalCategory.COLUMN_ID
    )
    val category: LocalCategory
)