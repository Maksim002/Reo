package ru.ktsstudio.core_data_measurement_impl.data.network.model

import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant

data class RemoteMeasurement(
    @SerializedName("mnoId") val mnoId: String,
    @SerializedName("measurementId") val measurementId: String,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("date") val date: Instant,
    @SerializedName("season") val season: String,
    @SerializedName("isPossible") val isPossible: Boolean,
    @SerializedName("containers") val containers: List<Container>?,
    @SerializedName("morphology") val morphologyList: List<Morphology>?,
    @SerializedName("media") val media: Media?,
    @SerializedName("impossibilityReason") val impossibilityReason: String?,
    @SerializedName("status") val measurementStatus: MeasurementStatus,
    @SerializedName("comment") val comment: String?,
    @SerializedName("revisionComment") val revisionComment: String?
) {
    data class Morphology(
        @SerializedName("group") val group: String,
        @SerializedName("groupTitle") val groupTitle: String,
        @SerializedName("subgroup") val subgroup: String?,
        @SerializedName("subgroupTitle") val subgroupTitle: String?,
        @SerializedName("dailyGainNetWeight") val dailyGainWeight: Float,
        @SerializedName("dailyGainVolume") val dailyGainVolume: Float
    )

    data class Container(
        @SerializedName("isUnique") val isUnique: Boolean,
        @SerializedName("containerType") val containerType: RemoteRegister.ContainerType,
        @SerializedName("wasteTypes") val wasteTypes: List<WasteType>?,
        @SerializedName("id") val mnoContainerId: String?,
        @SerializedName("containerName") val containerName: String?,
        @SerializedName("containerVolume") val containerVolume: Float?,
        @SerializedName("containerFullness") val containerFullness: Float?,
        @SerializedName("wasteVolume") val wasteVolume: Float?,
        @SerializedName("dailyGainVolume") val dailyGainVolume: Float?,
        @SerializedName("netWeight") val netWeight: Float?,
        @SerializedName("dailyGainNetWeight") val dailyGainNetWeight: Float?,
        @SerializedName("emptyContainerWeight") val emptyContainerWeight: Float?,
        @SerializedName("filledContainerWeight") val filledContainerWeight: Float?
    ) {

        data class WasteType(
            @SerializedName("id") val id: String,
            @SerializedName("name") val name: String,
            @SerializedName("nameOtherCategory") val nameOtherCategory: String?,
            @SerializedName("containerVolume") val containerVolume: Float?,
            @SerializedName("containerFullness") val containerFullness: Float?,
            @SerializedName("wasteVolume") val wasteVolume: Float,
            @SerializedName("dailyGainVolume") val dailyGainVolume: Float,
            @SerializedName("netWeight") val netWeight: Float,
            @SerializedName("dailyGainNetWeight") val dailyGainNetWeight: Float
        )
    }

    data class Media(
        @SerializedName("photos") val photos: List<MediaInstance>,
        @SerializedName("videos") val videos: List<MediaInstance>,
        @SerializedName("measurementActPhotos") val measurementActPhotos: List<MediaInstance>
    ) {

        data class MediaInstance(
            @SerializedName("id") val id: String,
            @SerializedName("url") val url: String,
            @SerializedName("latitude") val latitude: Double?,
            @SerializedName("longitude") val longitude: Double?,
            @SerializedName("date") val date: Instant?
        )
    }

    data class MeasurementStatus(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("order") val order: Int
    )
}