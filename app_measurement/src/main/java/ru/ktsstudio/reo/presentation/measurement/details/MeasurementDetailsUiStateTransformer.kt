package ru.ktsstudio.reo.presentation.measurement.details

import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItem
import ru.ktsstudio.common.ui.adapter.delegates.DividerItem
import ru.ktsstudio.common.ui.adapter.delegates.DoubleLabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.LabeledImageListItem
import ru.ktsstudio.common.ui.adapter.delegates.LabeledValueItem
import ru.ktsstudio.common.ui.adapter.delegates.titles.main.TitleItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementMedia
import ru.ktsstudio.core_data_measurement_api.data.model.MixedWasteContainer
import ru.ktsstudio.core_data_measurement_api.data.model.SeparateWasteContainer
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.domain.measurement.details.MeasurementDetailsFeature
import ru.ktsstudio.utilities.extensions.orDefault
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty
import java.util.Locale
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
internal class MeasurementDetailsUiStateTransformer @Inject constructor(
    private val resourceManager: ResourceManager
) : (MeasurementDetailsFeature.State) -> MeasurementDetailsUiState {

    private val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, eeee HH:mm")
        .withZone(ZoneId.systemDefault())
        .withLocale(Locale("ru"))

    override fun invoke(state: MeasurementDetailsFeature.State): MeasurementDetailsUiState {
        return MeasurementDetailsUiState(
            loading = state.loading,
            error = state.error,
            data = state.data?.let { details ->
                createAdapterItems(details, state.isPreviewMode)
            } ?: emptyList(),
            isMeasurementCreating = state.isCreating,
            isEditable = state.isEditable
        )
    }

    private fun createAdapterItems(details: Measurement, isPreviewMode: Boolean): List<Any> {
        return getInfoItems(details, isPreviewMode) +
            getContainersItems(details) +
            getMorphologyCardItems(details.morphologyList) +
            getMediaItems(details.media) +
            getCommentItem(
                title = resourceManager.getString(R.string.measurement_details_comment),
                comment = details.comment ?: details.impossibilityReason
            ) +
            getCommentItem(
                title = resourceManager.getString(R.string.measurement_details_revision_comment),
                comment = details.revisionComment
            )
    }

    private fun getInfoItems(details: Measurement, isPreviewMode: Boolean): List<Any> {
        if (isPreviewMode) {
            return listOf(
                TitleItem(resourceManager.getString(R.string.measurement_details_confirm_info_title))
            )
        }

        return listOf(
            TitleItem(resourceManager.getString(R.string.measurement_details_info_title)),
            LabeledValueItem(
                label = resourceManager.getString(R.string.measurement_details_season_label),
                value = details.season
            ),
            LabeledValueItem(
                label = resourceManager.getString(R.string.measurement_details_date_time_label),
                value = formatter.format(details.date)
            )
        )
    }

    private fun getContainersItems(details: Measurement): List<Any> {

        fun getContainersTitle(): Any? {
            val containersEmpty = details.mixedWasteContainers.isEmpty() ||
                details.separateWasteContainers.isEmpty()

            if (containersEmpty) return null
            return TitleItem(resourceManager.getString(R.string.measurement_details_containers_title))
        }

        fun getMixedWasteContainersItems(containers: List<MixedWasteContainer>): List<Any> {

            fun getContainerName(mixedContainer: MixedWasteContainer): Any? {
                return mixedContainer.containerName?.let {
                    LabeledValueItem(
                        label = resourceManager.getString(R.string.measurement_details_container_name_label),
                        value = it,
                        inCard = true
                    )
                }
            }

            fun getMixedContainerVolumeWithFullness(mixedContainer: MixedWasteContainer): Any {
                return mixedContainer.containerVolume?.let {
                    DoubleLabeledValueItem(
                        leftLabel = resourceManager.getString(R.string.measurement_details_container_volume_label),
                        leftValue = resourceManager.getString(
                            R.string.measurement_details_container_volume_value,
                            it
                        ),
                        rightLabel = resourceManager.getString(R.string.measurement_details_container_fullness_label),
                        rightValue = resourceManager.getString(
                            R.string.measurement_details_container_fullness_value,
                            mixedContainer.containerFullness ?: 0f
                        ), // TODO remove zeros
                        inCard = true
                    )
                } ?: LabeledValueItem(
                    label = resourceManager.getString(R.string.measurement_details_container_fullness_label),
                    value = resourceManager.getString(
                        R.string.measurement_details_container_fullness_value,
                        mixedContainer.containerFullness ?: 0f
                    ), // TODO remove zeros
                    inCard = true
                )
            }

            return containers.flatMap { mixedContainer: MixedWasteContainer ->
                listOf(
                    CardCornersItem(isTop = true),
                    CardTitleItem(text = resourceManager.getString(R.string.measurement_details_container_label))
                ) + getContainerName(mixedContainer) +
                    LabeledValueItem(
                        label = resourceManager.getString(R.string.measurement_details_container_type_label),
                        value = mixedContainer.containerType.name,
                        inCard = true
                    ) +
                    getMixedContainerVolumeWithFullness(mixedContainer) +
                    listOfNotNull(
                        LabeledValueItem(
                            label = resourceManager.getString(
                                R.string.measurement_details_container_empty_weight_title
                            ),
                            value = resourceManager.getString(
                                R.string.measurement_details_container_weight_value,
                                mixedContainer.emptyContainerWeight
                            ),
                            inCard = true
                        ).takeIf { mixedContainer.emptyContainerWeight != null },
                        LabeledValueItem(
                            label = resourceManager.getString(
                                R.string.measurement_details_container_filled_weight_title
                            ),
                            value = resourceManager.getString(
                                R.string.measurement_details_container_weight_value,
                                mixedContainer.filledContainerWeight
                            ),
                            inCard = true
                        ).takeIf { mixedContainer.filledContainerWeight != null },
                        CardTitleItem(
                            text = resourceManager.getString(R.string.measurement_details_container_waste_title)
                        ),
                        DoubleLabeledValueItem(
                            leftLabel = resourceManager.getString(
                                R.string.measurement_details_container_net_weight_label
                            ),
                            leftValue = resourceManager.getString(
                                R.string.measurement_details_container_weight_value,
                                mixedContainer.netWeight
                            ),
                            rightLabel = resourceManager.getString(
                                R.string.measurement_details_container_waste_volume_label
                            ),
                            rightValue = resourceManager.getString(
                                R.string.measurement_details_container_volume_value,
                                mixedContainer.wasteVolume
                            ),
                            inCard = true
                        ),
                        LabeledValueItem(
                            label = resourceManager.getString(
                                R.string.measurement_details_container_daily_gain_volume_label
                            ),
                            value = resourceManager.getString(
                                R.string.measurement_details_container_weight_value,
                                mixedContainer.dailyGainVolume
                            ),
                            inCard = true
                        ),
                        LabeledValueItem(
                            label = resourceManager.getString(
                                R.string.measurement_details_container_daily_gain_net_weight_label
                            ),
                            value = resourceManager.getString(
                                R.string.measurement_details_container_volume_value,
                                mixedContainer.dailyGainNetWeight
                            ),
                            inCard = true
                        ),
                        CardCornersItem(isTop = false)
                    )
            }
        }

        fun getSeparateContainersItems(containers: List<SeparateWasteContainer>): List<Any> {

            fun getContainerName(separateContainer: SeparateWasteContainer): Any? {
                return separateContainer.containerName?.let {
                    LabeledValueItem(
                        label = resourceManager.getString(R.string.measurement_details_container_name_label),
                        value = it,
                        inCard = true
                    )
                }
            }

            fun getContainerWasteTypeItems(wasteTypes: List<ContainerWasteType>?): Any? {
                return wasteTypes?.flatMapIndexed { index: Int, containerWasteType: ContainerWasteType ->
                    val wasteTypeIsNotLast = index < wasteTypes?.size?.let { it - 1 }.orDefault(0)

                    listOfNotNull(
                        LabeledValueItem(
                            label = resourceManager.getString(R.string.measurement_details_container_waste_type_label),
                            value = containerWasteType.categoryName,
                            inCard = true
                        ),
                        DoubleLabeledValueItem(
                            leftLabel = resourceManager.getString(R.string.measurement_details_container_volume_label),
                            leftValue = resourceManager.getString(
                                R.string.measurement_details_container_volume_value,
                                containerWasteType.containerVolume
                            ),
                            rightLabel = resourceManager.getString(
                                R.string.measurement_details_container_fullness_label
                            ),
                            rightValue = resourceManager.getString(
                                R.string.measurement_details_container_fullness_value,
                                containerWasteType.containerFullness
                            ),
                            inCard = true
                        ).takeIf {
                            containerWasteType.containerVolume != null &&
                                containerWasteType.containerFullness != null
                        },
                        DoubleLabeledValueItem(
                            leftLabel = resourceManager.getString(
                                R.string.measurement_details_container_net_weight_label
                            ),
                            leftValue = resourceManager.getString(
                                R.string.measurement_details_container_weight_value,
                                containerWasteType.netWeight
                            ),
                            rightLabel = resourceManager.getString(
                                R.string.measurement_details_container_waste_volume_label
                            ),
                            rightValue = resourceManager.getString(
                                R.string.measurement_details_container_volume_value,
                                containerWasteType.wasteVolume
                            ),
                            inCard = true
                        ),
                        LabeledValueItem(
                            label = resourceManager.getString(
                                R.string.measurement_details_container_daily_gain_volume_label
                            ),
                            value = resourceManager.getString(
                                R.string.measurement_details_container_weight_value,
                                containerWasteType.dailyGainVolume
                            ),
                            inCard = true
                        ),
                        LabeledValueItem(
                            label = resourceManager.getString(
                                R.string.measurement_details_container_daily_gain_net_weight_label
                            ),
                            value = resourceManager.getString(
                                R.string.measurement_details_container_volume_value,
                                containerWasteType.dailyGainNetWeight
                            ),
                            inCard = true
                        )
                    ) + if (wasteTypeIsNotLast) DividerItem(inCard = true) else null
                }
            }

            return containers.flatMap { separateContainer: SeparateWasteContainer ->
                listOf(
                    CardCornersItem(isTop = true),
                    CardTitleItem(text = resourceManager.getString(R.string.measurement_details_container_label))
                ) + getContainerName(separateContainer) +
                    LabeledValueItem(
                        label = resourceManager.getString(R.string.measurement_details_container_type_label),
                        value = separateContainer.containerType.name,
                        inCard = true
                    ) +
                    CardTitleItem(
                        text = resourceManager.getString(R.string.measurement_details_container_waste_title)
                    ) +
                    getContainerWasteTypeItems(separateContainer.wasteTypes) +
                    CardCornersItem(isTop = false)
            }
        }

        return listOf<Any>() +
            getContainersTitle() +
            getMixedWasteContainersItems(details.mixedWasteContainers) +
            getSeparateContainersItems(details.separateWasteContainers)
    }

    private fun getMorphologyCardItems(
        morphologyList: List<MorphologyItem>
    ): Any? {
        val title = listOf(
            TitleItem(resourceManager.getString(R.string.measurement_morphology_list_title))
        )
        val morphologyCard = morphologyList.takeIfNotEmpty()
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
                        resourceManager.getString(R.string.edit_morphology_waste_categories),
                        withAccent = true
                    )
                )
                val margin = resourceManager
                    .getDimensionPixelSize(R.dimen.default_double_padding)
                val cardBottom = listOf(CardEmptyLine(margin), CardCornersItem(isTop = false))

                cardTop + cardTitle + morphologyListItems + cardBottom
            }
            .orEmpty()

        return morphologyCard.takeIfNotEmpty()
            ?.let { title + morphologyCard }
    }

    private fun getMediaItems(media: MeasurementMedia?): Any? {
        return media?.let {
            listOfNotNull(
                LabeledImageListItem(
                    label = resourceManager.getString(R.string.measurement_details_photo_label),
                    photoUrls = it.photos.map { photo ->
                        photo.url ?: photo.cachedFile?.absolutePath.orEmpty()
                    }
                ).takeIf { it.photoUrls.isNotEmpty() },
                LabeledImageListItem(
                    label = resourceManager.getString(R.string.measurement_details_video_label),
                    photoUrls = it.videos.map { video ->
                        video.url ?: video.cachedFile?.absolutePath.orEmpty()
                    }
                ).takeIf { it.photoUrls.isNotEmpty() },
                LabeledImageListItem(
                    label = resourceManager.getString(R.string.measurement_details_act_photo_label),
                    photoUrls = it.measurementActPhotos.map { actPhoto ->
                        actPhoto.url ?: actPhoto.cachedFile?.absolutePath.orEmpty()
                    }
                ).takeIf { it.photoUrls.isNotEmpty() }
            )
        }
    }

    private fun getCommentItem(title: String, comment: String?): Any? {
        return comment.takeIf { it.isNullOrBlank().not() }
            ?.let {
                LabeledValueItem(
                    label = title,
                    value = it
                )
            }
    }

    private operator fun List<Any>.plus(element: Any?): List<Any> {
        return element?.let {
            if (it is List<*>) {
                ArrayList<Any>(this).apply {
                    addAll(it as List<Any>)
                }
            } else {
                this.plusElement(it)
            }
        } ?: this
    }
}
