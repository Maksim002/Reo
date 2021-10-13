package ru.ktsstudio.core_data_measurement_impl.data.network.model

import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant

/**
 * Created by Igor Park on 14/10/2020.
 */
data class MeasurementToSend(
    @SerializedName("measurementId")
    val measurementId: String?,
    @SerializedName("mnoId")
    val mnoId: String,
    @SerializedName("taskId")
    val taskId: String,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("date")
    val date: Instant,
    @SerializedName("isPossible")
    val isPossible: Boolean,
    @SerializedName("containers")
    val containers: List<ContainerToSend>,
    @SerializedName("morphology")
    val morphology: List<MorphologyToSend>,
    @SerializedName("photos")
    val photos: List<MediaInstanceToSend>?,
    @SerializedName("videos")
    val videos: List<MediaInstanceToSend>?,
    @SerializedName("measurementActPhotos")
    val measurementActPhotos: List<DocumentToSend>?,
    @SerializedName("impossibilityReason")
    val impossibilityReason: String?,
    @SerializedName("comment")
    val comment: String?
)

data class ContainerToSend(
    @SerializedName("containerId")
    val mnoContainerId: String?,
    @SerializedName("isUnique")
    val isUnique: Boolean,
    @SerializedName("containerName")
    val containerName: String?,
    @SerializedName("containerType")
    val containerType: String,
    @SerializedName("containerVolume")
    val containerVolume: Float?,
    @SerializedName("containerFullness")
    val containerFullness: Float?,
    @SerializedName("dailyGainNetWeight")
    val dailyGainNetWeight: Float?,
    @SerializedName("dailyGainVolume")
    val dailyGainVolume: Float?,
    @SerializedName("wasteTypes")
    val wasteTypes: List<WasteTypeToSend>?,
    @SerializedName("emptyWeight")
    val emptyWeight: Float?,
    @SerializedName("filledWeight")
    val filledWeight: Float?,
    @SerializedName("netWeight")
    val netWeight: Float?,
    @SerializedName("wasteVolume")
    val wasteVolume: Float?
)

data class ContainerType(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)

data class WasteTypeToSend(
    @SerializedName("id")
    val id: String,
    @SerializedName("nameOtherCategory")
    val name: String?,
    @SerializedName("containerVolume")
    val containerVolume: Float?,
    @SerializedName("containerFullness")
    val containerFullness: Float?,
    @SerializedName("dailyGainNetWeight")
    val dailyGainNetWeight: Float,
    @SerializedName("dailyGainVolume")
    val dailyGainVolume: Float,
    @SerializedName("netWeight")
    val netWeight: Float,
    @SerializedName("wasteVolume")
    val wasteVolume: Float
)

data class MorphologyToSend(
    @SerializedName("group")
    val group: String,
    @SerializedName("subgroup")
    val subgroup: String?,
    @SerializedName("dailyGainNetWeight")
    val dailyGainWeight: Float,
    @SerializedName("dailyGainVolume")
    val dailyGainVolume: Float
)

data class MediaInstanceToSend(
    @SerializedName("id")
    val id: String,
    @SerializedName("date")
    val date: Instant,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)

data class DocumentToSend(
    @SerializedName("id")
    val id: String
)


