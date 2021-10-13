package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import android.content.Context
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementComposite
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainer
import java.io.File
import javax.inject.Inject

/**
 * Created by Igor Park on 15/10/2020.
 */
class MeasurementCompositeMapper @Inject constructor(
    private val context: Context
) : Mapper<
    LocalMeasurementWithRelations,
    MeasurementComposite
    > {

    override fun map(item: LocalMeasurementWithRelations): MeasurementComposite = with(item) {

        val mediaGroupedByCategories = medias.groupBy { it.measurementMedia.mediaCategory }

        fun getMediaByCategory(category: MeasurementMediaCategory): List<MeasurementComposite.Media> {
            return mediaGroupedByCategories[category]
                ?.map { localMedia ->
                    val media = localMedia.media
                    MeasurementComposite.Media(
                        id = media.localId,
                        remoteUrl = media.url,
                        cachedFile = media.cachedFilePath?.let { mediaFilePath ->
                            File(context.getExternalFilesDir(null), mediaFilePath)
                        }
                    )
                }
                .orEmpty()
        }

        val separateContainers = separateWasteContainers.map { separateContainerWithRelations ->
            val container = separateContainerWithRelations.container
            val registryValue = separateContainerWithRelations.mnoContainer
            getCompositeContainer(
                containerLocalId = container.localId,
                isUnique = container.isUnique,
                isSeparate = true,
                registryValue = registryValue?.container,
                uniqueVolume = container.containerVolume,
                uniqueName = container.containerName
            )
        }

        val mixedContainers = mixedWasteContainers.map { mixedContainerWithRelations ->
            val container = mixedContainerWithRelations.container
            val registryValue = mixedContainerWithRelations.mnoContainer?.container

            getCompositeContainer(
                containerLocalId = container.localId,
                isUnique = container.isUnique,
                isSeparate = false,
                registryValue = registryValue,
                uniqueVolume = container.containerVolume,
                uniqueName = container.containerName
            )
        }

        val morphologyList = morphologyList.map {
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
        }

        MeasurementComposite(
            localId = measurement.localId,
            photos = getMediaByCategory(MeasurementMediaCategory.PHOTO),
            videos = getMediaByCategory(MeasurementMediaCategory.VIDEO),
            actPhotos = getMediaByCategory(MeasurementMediaCategory.ACT_PHOTO),
            containers = separateContainers + mixedContainers,
            comment = measurement.comment.orEmpty(),
            morphologyItemList = morphologyList
        )
    }

    private fun getCompositeContainer(
        containerLocalId: Long,
        isSeparate: Boolean,
        isUnique: Boolean,
        registryValue: LocalMnoContainer?,
        uniqueVolume: Float?,
        uniqueName: String?
    ): MeasurementComposite.Container {
        val name = uniqueName.takeIf { isUnique }
            ?: registryValue?.name.orEmpty()

        val volume = uniqueVolume.takeIf { isUnique }
            ?: registryValue?.volume


        return MeasurementComposite.Container(
            id = containerLocalId,
            name = name,
            volume = volume ?: 0F, //todo remove zeros
            isSeparate = isSeparate
        )
    }
}