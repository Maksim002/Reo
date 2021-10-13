package ru.ktsstudio.core_data_measurement_impl.data.db.model

data class CompositeMedia(
    val photos: List<LocalMedia>,
    val videos: List<LocalMedia>,
    val actPhotos: List<LocalMedia>,
    val measurementId: Long
)
