package ru.ktsstudio.core_data_measurement_api.domain

import java.io.File

data class MeasurementComposite(
    val localId: Long,
    val photos: List<Media>,
    val videos: List<Media>,
    val actPhotos: List<Media>,
    val containers: List<Container>,
    val morphologyItemList: List<MorphologyItem>,
    val comment: String
) {
    data class Container(
        val id: Long,
        val name: String,
        val volume: Float?,
        val isSeparate: Boolean
    )

    data class Media(
        val id: Long,
        val remoteUrl: String?,
        val cachedFile: File?
    )

    fun isEmpty(): Boolean {
        return photos.isEmpty() &&
            videos.isEmpty() &&
            actPhotos.isEmpty() &&
            containers.isEmpty() &&
            morphologyItemList.isEmpty() &&
            comment.isBlank()
    }
}
