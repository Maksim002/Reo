package ru.ktsstudio.core_data_measurement_api.data.model

import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem

data class Measurement(
    val mnoId: String,
    val measurementLocalId: Long,
    val measurementRemoteId: String?,
    val gpsPoint: GpsPoint?,
    val season: String,
    val date: Instant,
    val isPossible: Boolean,
    val status: MeasurementStatus,
    val media: MeasurementMedia?,
    val morphologyList: List<MorphologyItem>,
    val separateWasteContainers: List<SeparateWasteContainer>,
    val mixedWasteContainers: List<MixedWasteContainer>,
    val impossibilityReason: String?,
    val comment: String?,
    val revisionComment: String?
)

data class MeasurementStatus(
    val id: String,
    val name: String,
    val order: Int
)

data class MeasurementMedia(
    val photos: List<Photo>,
    val videos: List<Video>,
    val measurementActPhotos: List<Photo>
)

data class Morphology(
    val localId: Long,
    val groupId: String,
    val subGroupId: String?,
    val dailyGainWeight: Float,
    val dailyGainVolume: Float,
    val draftState: DraftState
)

data class MixedWasteContainer(
    val localId: Long,
    val isUnique: Boolean,
    val mnoContainerId: String?,
    val containerType: ContainerType,
    val containerName: String?,
    val containerVolume: Float?,
    val containerFullness: Float?,
    val wasteVolume: Float,
    val dailyGainVolume: Float,
    val netWeight: Float,
    val dailyGainNetWeight: Float,
    val emptyContainerWeight: Float?,
    val filledContainerWeight: Float?
)

data class SeparateWasteContainer(
    val localId: Long,
    val isUnique: Boolean,
    val mnoContainerId: String?,
    val containerName: String?,
    val containerVolume: Float?,
    val containerType: ContainerType,
    val wasteTypes: List<ContainerWasteType>?
)

data class ContainerWasteType(
    val localId: String,
    val categoryId: String,
    val categoryName: String,
    val nameOtherCategory: String?,
    val containerVolume: Float?,
    val containerFullness: Float?,
    val wasteVolume: Float,
    val dailyGainVolume: Float,
    val netWeight: Float,
    val dailyGainNetWeight: Float,
    val isOtherCategory: Boolean,
    val draftState: DraftState
)