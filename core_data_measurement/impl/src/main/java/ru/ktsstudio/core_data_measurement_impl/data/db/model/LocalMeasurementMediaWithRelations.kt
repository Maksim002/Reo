package ru.ktsstudio.core_data_measurement_impl.data.db.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
data class LocalMeasurementMediaWithRelations(
    @Embedded
    val measurementMedia: LocalMeasurementMedia,
    @Relation(
        entity = LocalMedia::class,
        parentColumn = LocalMeasurementMedia.COLUMN_MEDIA_LOCAL_ID,
        entityColumn = LocalMedia.COLUMN_LOCAL_ID
    )
    val media: LocalMedia
)