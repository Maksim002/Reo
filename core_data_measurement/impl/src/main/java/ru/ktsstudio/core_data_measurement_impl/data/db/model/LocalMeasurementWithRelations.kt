package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */

data class LocalMeasurementWithRelations(
    @Embedded
    val measurement: LocalMeasurement,
    @Relation(
        entity = LocalMno::class,
        parentColumn = LocalMeasurement.COLUMN_MNO_ID,
        entityColumn = LocalMno.COLUMN_ID
    )
    val mno: LocalMno,
    @Relation(
        entity = LocalMeasurementStatus::class,
        parentColumn = LocalMeasurement.COLUMN_MEASUREMENT_STATUS_ID,
        entityColumn = LocalMeasurementStatus.COLUMN_ID
    )
    val status: LocalMeasurementStatus,
    @Relation(
        entity = LocalMeasurementMedia::class,
        parentColumn = LocalMeasurement.COLUMN_LOCAL_ID,
        entityColumn = LocalMeasurementMedia.COLUMN_MEASUREMENT_ID
    )
    val medias: List<LocalMeasurementMediaWithRelations>,
    @Relation(
        entity = LocalSeparateWasteContainer::class,
        parentColumn = LocalMeasurement.COLUMN_LOCAL_ID,
        entityColumn = LocalSeparateWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID
    )
    val separateWasteContainers: List<LocalSeparateWasteContainerWithRelations>,
    @Relation(
        entity = LocalMixedWasteContainer::class,
        parentColumn = LocalMeasurement.COLUMN_LOCAL_ID,
        entityColumn = LocalMixedWasteContainer.COLUMN_MEASUREMENT_LOCAL_ID
    )
    val mixedWasteContainers: List<LocalMixedWasteContainerWithRelations>,
    @Relation(
        entity = LocalMorphology::class,
        parentColumn = LocalMeasurement.COLUMN_LOCAL_ID,
        entityColumn = LocalMorphology.COLUMN_MEASUREMENT_LOCAL_ID
    )
    val morphologyList: List<LocalMorphologyWithRelation>
)