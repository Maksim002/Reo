package ru.ktsstudio.reo.presentation.measurement.create_measurement

import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItem
import ru.ktsstudio.common.ui.adapter.delegates.DividerItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.small.SmallTitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementComposite
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.create_measurement.CreateMeasurementFeature
import ru.ktsstudio.reo.domain.measurement.create_measurement.MediaProcessingState
import ru.ktsstudio.reo.ui.measurement.create.adapters.CommentaryItem
import ru.ktsstudio.reo.ui.measurement.create.adapters.MeasurementMediaListItem
import ru.ktsstudio.utilities.extensions.orFalse
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty

/**
 * Created by Igor Park on 16/10/2020.
 */
class CreateMeasurementUiStateTransformer(
    private val resources: ResourceManager
) : (CreateMeasurementFeature.State) -> CreateMeasurementUiState {

    override fun invoke(state: CreateMeasurementFeature.State): CreateMeasurementUiState =
        with(state) {
            if (measurement == null) return@with CreateMeasurementUiState(
                measurementId = null,
                fields = emptyList(),
                isLoading = isLoading,
                isReady = false,
                error = error
            )

            val title = listOf(
                TitleItem(resources.getString(R.string.measurement_details_containers_title))
            )
            val containersBlock = listOf(
                SmallTitleItem(resources.getString(R.string.measurement_containers_list_title))
            ) + measurement.containers + listOf(
                AddEntityItem(
                    text = resources.getString(R.string.measurement_add_container),
                    icon = R.drawable.ic_plus,
                    qualifier = MeasurementEntity.CONTAINER
                )
            )

            val morphologyBlock = listOf(
                SmallTitleItem(resources.getString(R.string.measurement_morphology_list_title))
            ) + getMorphologyCardItems(measurement.morphologyItemList) + listOf(
                AddEntityItem(
                    text = if (measurement.morphologyItemList.isEmpty()) {
                        resources.getString(R.string.measurement_add_morphology)
                    } else {
                        resources.getString(R.string.measurement_edit_morphology)
                    },
                    icon = if (measurement.morphologyItemList.isEmpty()) {
                        R.drawable.ic_plus
                    } else {
                        R.drawable.ic_edit
                    },
                    qualifier = MeasurementEntity.MORPHOLOGY
                )
            )

            val comment = listOf(
                CommentaryItem(
                    text = measurement.comment
                )
            )

            val medias = prepareMediaBlock(measurement, fileProcessingState)

            val isReady = measurement.containers.isNotEmpty()

            return CreateMeasurementUiState(
                measurementId = measurement.localId,
                fields = title + containersBlock + morphologyBlock + comment + medias,
                isLoading = isLoading,
                error = error,
                isReady = isReady
            )
        }

    private fun prepareMediaBlock(
        measurement: MeasurementComposite,
        fileProcessingState: Map<Long, MediaProcessingState>
    ): List<Any> {
        fun mapToUi(media: MeasurementComposite.Media): MeasurementMediaUi {
            return MeasurementMediaUi.Media(
                media = media,
                isLoading = fileProcessingState[media.id]
                    ?.let { it is MediaProcessingState.Processing }
                    .orFalse()
            )
        }

        val addPhotoBtn = listOf(
            MeasurementMediaUi.AddItem(category = MeasurementMediaCategory.PHOTO)
        )
        val addVideoBtn = listOf(
            MeasurementMediaUi.AddItem(category = MeasurementMediaCategory.VIDEO)
        )
        val addActBtn = listOf(
            MeasurementMediaUi.AddItem(category = MeasurementMediaCategory.ACT_PHOTO)
        )

        val actPhotos = measurement.actPhotos.map(::mapToUi)
        val photos = measurement.photos.map(::mapToUi)
        val videos = measurement.videos.map(::mapToUi)
        return listOf(
            MeasurementMediaListItem(
                title = resources.getString(R.string.measurement_details_photo_label),
                mediaList = addPhotoBtn + photos,
                category = MeasurementMediaCategory.PHOTO
            ),
            MeasurementMediaListItem(
                title = resources.getString(R.string.measurement_details_video_label),
                mediaList = addVideoBtn + videos,
                category = MeasurementMediaCategory.PHOTO
            ),
            MeasurementMediaListItem(
                title = resources.getString(R.string.measurement_details_act_photo_label),
                mediaList = addActBtn + actPhotos,
                category = MeasurementMediaCategory.ACT_PHOTO
            )
        )
    }

    private fun getMorphologyCardItems(
        morphologyList: List<MorphologyItem>
    ): List<Any> {
        return morphologyList.takeIfNotEmpty()
            ?.let { morphologies ->
                val morphologyListItems =
                    morphologies.fold(mutableListOf<Any>()) { acc, morphology ->
                        acc.apply {
                            add(morphology)
                            add(DividerItem(inCard = true))
                        }
                    }
                        .dropLast(1)

                val cardTop = listOf(
                    CardCornersItem(isTop = true)
                )
                val cardTitle = listOf(
                    CardTitleItem(
                        resources.getString(R.string.edit_morphology_waste_categories),
                        withAccent = true
                    )
                )
                val margin = resources
                    .getDimensionPixelSize(R.dimen.default_double_padding)
                val cardBottom = listOf(CardEmptyLine(margin), CardCornersItem(isTop = false))

                cardTop + cardTitle + morphologyListItems + cardBottom
            }
            .orEmpty()
    }
}
