package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import android.content.Context
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementMedia
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus
import ru.ktsstudio.core_data_measurement_api.data.model.MixedWasteContainer
import ru.ktsstudio.core_data_measurement_api.data.model.Morphology
import ru.ktsstudio.core_data_measurement_api.data.model.Photo
import ru.ktsstudio.core_data_measurement_api.data.model.SeparateWasteContainer
import ru.ktsstudio.core_data_measurement_api.data.model.Video
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementMediaWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementWithRelations
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty
import java.io.File
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
class MeasurementWithRelationsMapper @Inject constructor(
    private val context: Context
) : Mapper<LocalMeasurementWithRelations, Measurement> {

    override fun map(item: LocalMeasurementWithRelations): Measurement = with(item) {
        Measurement(
            mnoId = measurement.mnoId,
            measurementLocalId = measurement.localId,
            measurementRemoteId = measurement.remoteId,
            gpsPoint = measurement.gpsPoint,
            season = measurement.season,
            date = measurement.date,
            isPossible = measurement.isPossible,
            status = MeasurementStatus(
                id = status.id,
                name = status.name,
                order = status.order
            ),
            media = medias.takeIfNotEmpty()?.let {
                MeasurementMedia(
                    photos = mapPhoto(
                        medias,
                        measurementMediaCategory = MeasurementMediaCategory.PHOTO
                    ),
                    videos = mapVideo(medias),
                    measurementActPhotos = mapPhoto(
                        medias,
                        measurementMediaCategory = MeasurementMediaCategory.ACT_PHOTO
                    )
                )
            },
            morphologyList = morphologyList.map {
                MorphologyItem(
                    localId = it.morphology.localId,
                    group = WasteGroup(it.wasteGroup.id, it.wasteGroup.name),
                    subgroup = it.wasteSubgroup
                        ?.let {
                            WasteSubgroup(
                                id = it.id,
                                name = it.name,
                                groupId = it.groupId
                            )
                        },
                    dailyGainWeight = it.morphology.dailyGainWeight,
                    dailyGainVolume = it.morphology.dailyGainVolume,
                    draftState = it.morphology.draftState
                )
            },
            separateWasteContainers = separateWasteContainers.map { separateWasteContainerWithRelations ->
                val separateWasteContainer = separateWasteContainerWithRelations.container
                val containerType = separateWasteContainerWithRelations.containerType
                val mnoContainer = separateWasteContainerWithRelations.mnoContainer?.container
                SeparateWasteContainer(
                    localId = separateWasteContainer.localId,
                    isUnique = separateWasteContainer.isUnique,
                    mnoContainerId = separateWasteContainer.mnoContainerId,
                    containerName = if (separateWasteContainer.isUnique) {
                        separateWasteContainer.containerName
                    } else {
                        mnoContainer?.name
                    },
                    containerVolume = if (separateWasteContainer.isUnique) {
                        separateWasteContainer.containerVolume
                    } else {
                        mnoContainer?.volume
                    },
                    containerType = ContainerType(
                        id = containerType.id,
                        name = separateWasteContainerWithRelations.containerType.name,
                        isSeparate = separateWasteContainerWithRelations.containerType.isSeparate
                    ),
                    wasteTypes = separateWasteContainerWithRelations.wasteTypes.map { wasteTypeWithRelations ->
                        val wasteType = wasteTypeWithRelations.wasteType
                        ContainerWasteType(
                            localId = wasteType.localId,
                            categoryId = wasteType.categoryId,
                            categoryName = wasteType.categoryName,
                            nameOtherCategory = wasteType.nameOtherCategory,
                            containerVolume = wasteType.containerVolume,
                            containerFullness = wasteType.containerFullness,
                            wasteVolume = wasteType.wasteVolume,
                            dailyGainVolume = wasteType.dailyGainVolume,
                            netWeight = wasteType.netWeight,
                            dailyGainNetWeight = wasteType.dailyGainNetWeight,
                            isOtherCategory = wasteType.nameOtherCategory.isNullOrBlank().not(),
                            draftState = wasteType.draftState
                        )
                    }
                )
            },
            mixedWasteContainers = mixedWasteContainers.map { mixedWasteContainerWithRelations ->
                val mixedWasteContainer = mixedWasteContainerWithRelations.container
                val containerType = mixedWasteContainerWithRelations.containerType
                val mnoContainer = mixedWasteContainerWithRelations.mnoContainer?.container
                MixedWasteContainer(
                    localId = mixedWasteContainer.localId,
                    isUnique = mixedWasteContainer.isUnique,
                    mnoContainerId = mixedWasteContainer.mnoContainerId,
                    containerName = if (mixedWasteContainer.isUnique) {
                        mixedWasteContainer.containerName
                    } else {
                        mnoContainer?.name
                    },
                    containerVolume = if (mixedWasteContainer.isUnique) {
                        mixedWasteContainer.containerVolume
                    } else {
                        mnoContainer?.volume
                    },
                    containerType = ContainerType(
                        id = containerType.id,
                        name = containerType.name,
                        isSeparate = containerType.isSeparate
                    ),
                    containerFullness = mixedWasteContainer.containerFullness,
                    wasteVolume = mixedWasteContainer.wasteVolume,
                    dailyGainVolume = mixedWasteContainer.dailyGainVolume,
                    netWeight = mixedWasteContainer.netWeight,
                    dailyGainNetWeight = mixedWasteContainer.dailyGainNetWeight,
                    emptyContainerWeight = mixedWasteContainer.emptyContainerWeight,
                    filledContainerWeight = mixedWasteContainer.filledContainerWeight
                )
            },
            impossibilityReason = measurement.impossibilityReason,
            comment = measurement.comment,
            revisionComment = measurement.revisionComment
        )
    }

    private fun mapPhoto(
        medias: List<LocalMeasurementMediaWithRelations>,
        measurementMediaCategory: MeasurementMediaCategory
    ): List<Photo> {
        return medias.filter { it.measurementMedia.mediaCategory == measurementMediaCategory }
            .map {
                Photo(
                    id = it.media.remoteId,
                    url = it.media.url,
                    cachedFile = it.media.cachedFilePath?.let { mediaFilePath ->
                        File(context.getExternalFilesDir(null), mediaFilePath)
                    },
                    date = it.media.date,
                    gpsPoint = it.media.gpsPoint
                )
            }
    }

    private fun mapVideo(
        medias: List<LocalMeasurementMediaWithRelations>
    ): List<Video> {
        return medias.filter { it.measurementMedia.mediaCategory == MeasurementMediaCategory.VIDEO }
            .map {
                Video(
                    id = it.media.remoteId,
                    url = it.media.url,
                    cachedFile = it.media.cachedFilePath?.let { mediaFilePath ->
                        File(context.getExternalFilesDir(null), mediaFilePath)
                    },
                    date = it.media.date,
                    gpsPoint = it.media.gpsPoint
                )
            }
    }
}