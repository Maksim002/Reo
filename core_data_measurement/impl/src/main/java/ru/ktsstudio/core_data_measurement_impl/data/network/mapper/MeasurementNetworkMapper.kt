package ru.ktsstudio.core_data_measurement_impl.data.network.mapper

import ru.ktsstudio.common.data.models.DraftState
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.id.IdGenerator
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementMedia
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus
import ru.ktsstudio.core_data_measurement_api.data.model.MixedWasteContainer
import ru.ktsstudio.core_data_measurement_api.data.model.Photo
import ru.ktsstudio.core_data_measurement_api.data.model.SeparateWasteContainer
import ru.ktsstudio.core_data_measurement_api.data.model.Video
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteMeasurement
import javax.inject.Inject

class MeasurementNetworkMapper @Inject constructor(
    private val idGenerator: IdGenerator
) : Mapper<RemoteMeasurement, Measurement> {

    override fun map(item: RemoteMeasurement): Measurement {
        return with(item) {
            val (separateWasteContainers, mixedContainers) = containers.orEmpty().partition {
                it.containerType.isSeparate
            }

            Measurement(
                mnoId = mnoId,
                measurementLocalId = 0L,
                measurementRemoteId = measurementId,
                gpsPoint = if (latitude == null || longitude == null) {
                    null
                } else {
                    GpsPoint(latitude, longitude)
                },
                season = season,
                date = date,
                isPossible = isPossible,
                status = MeasurementStatus(
                    id = measurementStatus.id,
                    name = measurementStatus.name,
                    order = measurementStatus.order
                ),
                morphologyList = morphologyList.orEmpty()
                    .map { morphology ->
                        val subgroupId = morphology.subgroup
                        val subgroupTitle = morphology.subgroupTitle
                        MorphologyItem(
                            localId = 0L,
                            group = WasteGroup(
                                id = morphology.group,
                                name = morphology.groupTitle
                            ),
                            subgroup = if (subgroupId != null && subgroupTitle != null) {
                                WasteSubgroup(
                                    id = subgroupId,
                                    name = subgroupTitle,
                                    groupId = morphology.group
                                )
                            } else {
                                null
                            },
                            dailyGainWeight = morphology.dailyGainWeight,
                            dailyGainVolume = morphology.dailyGainVolume,
                            draftState = DraftState.IDLE
                        )
                    },
                media = MeasurementMedia(
                    photos = media?.photos.orEmpty()
                        .map { media ->
                            Photo(
                                id = media.id,
                                url = media.url,
                                cachedFile = null,
                                gpsPoint = if (media.latitude == null || media.longitude == null) {
                                    null
                                } else {
                                    GpsPoint(media.latitude, media.longitude)
                                },
                                date = media.date
                            )
                        },
                    videos = media?.videos.orEmpty()
                        .map { media ->
                            Video(
                                id = media.id,
                                url = media.url,
                                cachedFile = null,
                                gpsPoint = if (media.latitude == null || media.longitude == null) {
                                    null
                                } else {
                                    GpsPoint(media.latitude, media.longitude)
                                },
                                date = media.date
                            )
                        },
                    measurementActPhotos = media?.measurementActPhotos.orEmpty()
                        .map { media ->
                            Photo(
                                id = media.id,
                                url = media.url,
                                cachedFile = null,
                                gpsPoint = null,
                                date = null
                            )
                        }
                ),
                separateWasteContainers = separateWasteContainers.map { container ->
                    SeparateWasteContainer(
                        localId = 0L,
                        isUnique = container.isUnique,
                        mnoContainerId = container.mnoContainerId,
                        containerName = container.containerName.takeIf { container.isUnique },
                        containerVolume = container.containerVolume.takeIf { container.isUnique },
                        containerType = ContainerType(
                            id = container.containerType.id,
                            name = container.containerType.name,
                            isSeparate = container.containerType.isSeparate
                        ),
                        wasteTypes = container.wasteTypes?.map { wasteType ->
                            ContainerWasteType(
                                localId = idGenerator.generateStringId(),
                                categoryId = wasteType.id,
                                categoryName = wasteType.name,
                                nameOtherCategory = wasteType.nameOtherCategory,
                                containerVolume = wasteType.containerVolume,
                                containerFullness = wasteType.containerFullness,
                                wasteVolume = wasteType.wasteVolume,
                                dailyGainVolume = wasteType.dailyGainVolume,
                                netWeight = wasteType.netWeight,
                                dailyGainNetWeight = wasteType.dailyGainNetWeight,
                                isOtherCategory = wasteType.nameOtherCategory.isNullOrBlank().not(),
                                draftState = DraftState.IDLE
                            )
                        }
                    )
                },
                mixedWasteContainers = mixedContainers.mapNotNull { container ->
                    MixedWasteContainer(
                        localId = 0L,
                        isUnique = container.isUnique,
                        mnoContainerId = container.mnoContainerId,
                        containerType = ContainerType(
                            id = container.containerType.id,
                            name = container.containerType.name,
                            isSeparate = container.containerType.isSeparate
                        ),
                        containerName = container.containerName.takeIf { container.isUnique },
                        containerVolume = container.containerVolume.takeIf { container.isUnique },
                        containerFullness = container.containerFullness,
                        wasteVolume = container.wasteVolume ?: return@mapNotNull null,
                        dailyGainVolume = container.dailyGainVolume ?: return@mapNotNull null,
                        netWeight = container.netWeight ?: return@mapNotNull null,
                        dailyGainNetWeight = container.dailyGainNetWeight ?: return@mapNotNull null,
                        emptyContainerWeight = container.emptyContainerWeight,
                        filledContainerWeight = container.filledContainerWeight
                    )
                },
                impossibilityReason = impossibilityReason.takeIf { it.isNullOrBlank().not() },
                comment = comment.takeIf { it.isNullOrBlank().not() },
                revisionComment = revisionComment
            )
        }
    }
}