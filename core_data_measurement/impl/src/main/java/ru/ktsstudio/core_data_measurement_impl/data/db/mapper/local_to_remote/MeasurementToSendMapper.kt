package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_remote

import ru.ktsstudio.common.utils.checkValue
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerWasteTypeWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementMediaWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMixedWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalSeparateWasteContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.network.model.ContainerToSend
import ru.ktsstudio.core_data_measurement_impl.data.network.model.DocumentToSend
import ru.ktsstudio.core_data_measurement_impl.data.network.model.MeasurementToSend
import ru.ktsstudio.core_data_measurement_impl.data.network.model.MediaInstanceToSend
import ru.ktsstudio.core_data_measurement_impl.data.network.model.MorphologyToSend
import ru.ktsstudio.core_data_measurement_impl.data.network.model.WasteTypeToSend
import javax.inject.Inject

class MeasurementToSendMapper @Inject constructor() :
    Mapper<LocalMeasurementWithRelations, MeasurementToSend> {

    override fun map(item: LocalMeasurementWithRelations): MeasurementToSend = with(item) {
        MeasurementToSend(
            measurementId = measurement.remoteId,
            taskId = mno.taskIds.first(),
            mnoId = measurement.mnoId,
            latitude = measurement.gpsPoint?.lat,
            longitude = measurement.gpsPoint?.lng,
            date = measurement.date,
            isPossible = measurement.isPossible,
            impossibilityReason = measurement.impossibilityReason,
            comment = measurement.comment,
            morphology = morphologyList.map {
                MorphologyToSend(
                    group = it.wasteGroup.id,
                    subgroup = it.wasteSubgroup?.id,
                    dailyGainVolume = it.morphology.dailyGainVolume,
                    dailyGainWeight = it.morphology.dailyGainWeight
                )
            },
            photos = mapMedia(
                medias,
                measurementMediaCategory = MeasurementMediaCategory.PHOTO
            ),
            videos = mapMedia(
                medias,
                measurementMediaCategory = MeasurementMediaCategory.VIDEO
            ),
            measurementActPhotos = mapDocument(medias),
            containers = mapContainers(mixedWasteContainers, separateWasteContainers)
        )
    }

    private fun mapContainers(
        mixedWasteContainers: List<LocalMixedWasteContainerWithRelations>,
        separateWasteContainers: List<LocalSeparateWasteContainerWithRelations>
    ): List<ContainerToSend> {
        val separateContainers = separateWasteContainers.map { separateContainerWithRelations ->
            with(separateContainerWithRelations.container) {
                ContainerToSend(
                    mnoContainerId = if (isUnique) mnoUniqueContainerId else mnoContainerId,
                    isUnique = isUnique,
                    containerType = containerTypeId,
                    containerName = if (isUnique) {
                        containerName
                    } else {
                        separateContainerWithRelations.mnoContainer?.container?.name
                    },
                    containerVolume = if (isUnique) {
                        containerVolume
                    } else {
                        separateContainerWithRelations.mnoContainer?.container?.volume
                    },
                    containerFullness = null,
                    dailyGainNetWeight = null,
                    dailyGainVolume = null,
                    emptyWeight = null,
                    filledWeight = null,
                    netWeight = null,
                    wasteVolume = null,
                    wasteTypes = mapContainerWasteTypes(separateContainerWithRelations.wasteTypes)
                )
            }
        }
        val mixedContainers = mixedWasteContainers.map { mixedWasteContainerWithRelations ->
            with(mixedWasteContainerWithRelations.container) {
                ContainerToSend(
                    mnoContainerId = if (isUnique) mnoUniqueContainerId else mnoContainerId,
                    isUnique = isUnique,
                    containerType = containerTypeId,
                    containerName = if (isUnique) {
                        containerName
                    } else {
                        mixedWasteContainerWithRelations.mnoContainer?.container?.name
                    },
                    containerVolume = if (isUnique) {
                        containerVolume
                    } else {
                        mixedWasteContainerWithRelations.mnoContainer?.container?.volume
                    },
                    containerFullness = containerFullness,
                    dailyGainNetWeight = dailyGainNetWeight,
                    dailyGainVolume = dailyGainVolume,
                    emptyWeight = emptyContainerWeight,
                    filledWeight = filledContainerWeight,
                    netWeight = netWeight,
                    wasteVolume = wasteVolume,
                    wasteTypes = null
                )
            }
        }
        return separateContainers + mixedContainers
    }

    private fun mapMedia(
        medias: List<LocalMeasurementMediaWithRelations>,
        measurementMediaCategory: MeasurementMediaCategory
    ): List<MediaInstanceToSend> {
        return medias.filter { it.measurementMedia.mediaCategory == measurementMediaCategory }
            .map {
                it.media
            }
            .map { media ->
                MediaInstanceToSend(
                    id = checkValue(media.remoteId, "mediaID=${media.localId}.remoteId"),
                    date = checkValue(media.date, "mediaID=${media.localId}.date"),
                    latitude = media.gpsPoint?.lat,
                    longitude = media.gpsPoint?.lng
                )
            }
    }

    private fun mapDocument(
        medias: List<LocalMeasurementMediaWithRelations>
    ): List<DocumentToSend> {
        return medias.filter { it.measurementMedia.mediaCategory == MeasurementMediaCategory.ACT_PHOTO }
            .map { it.media.remoteId }
            .map(::DocumentToSend)
    }

    private fun mapContainerWasteTypes(
        wasteTypes: List<LocalContainerWasteTypeWithRelation>
    ): List<WasteTypeToSend> {
        return wasteTypes.map { containerWasteTypeWithRelation ->
            with(containerWasteTypeWithRelation.wasteType) {
                WasteTypeToSend(
                    id = categoryId,
                    name = nameOtherCategory,
                    containerVolume = containerVolume,
                    containerFullness = containerFullness,
                    dailyGainNetWeight = dailyGainNetWeight,
                    dailyGainVolume = dailyGainVolume,
                    netWeight = netWeight,
                    wasteVolume = wasteVolume
                )
            }
        }
    }
}
