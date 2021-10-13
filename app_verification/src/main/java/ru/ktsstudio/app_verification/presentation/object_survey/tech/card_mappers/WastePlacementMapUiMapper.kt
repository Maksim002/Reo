package ru.ktsstudio.app_verification.presentation.object_survey.tech.card_mappers

import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.DateValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDataType
import ru.ktsstudio.app_verification.domain.object_survey.tech.models.TechnicalSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.TechnicalCardType
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledDateItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import ru.ktsstudio.utilities.extensions.requireNotNull

/**
 * @author Maxim Ovchinnikov on 18.12.2020.
 */
class WastePlacementMapUiMapper(private val resourceManager: ResourceManager) {

    @OptIn(ExperimentalStdlibApi::class)
    fun map(
        isChecked: Boolean,
        items: List<WastePlacementMap>
    ): List<Any> {
        return buildList {
            add(
                SubtitleItemWithCheck(
                    checkableValueConsumer = TechnicalSurveyDataType.WastePlacementMapType(isChecked),
                    title = resourceManager.getString(R.string.survey_technical_waste_placement_map_title)
                )
            )
            items.takeIf { it.isNotEmpty() }
                ?.let {
                    addItemsCount(items.size)
                    addNestingWastePlacementMaps(items)
                }
                ?: add(
                    InnerMediumTitle(
                        text = resourceManager.getString(R.string.survey_technical_waste_placement_map_empty),
                        withAccent = false
                    )
                )
            add(
                AddEntityItem(
                    nested = true,
                    text = resourceManager.getString(R.string.survey_technical_waste_placement_map_add),
                    qualifier = TechnicalCardType.WASTE_PLACEMENT_MAP
                )
            )
        }
    }

    private fun MutableList<Any>.addItemsCount(count: Int?) {
        count?.let {
            add(
                InnerLabeledEditItem(
                    label = resourceManager.getString(R.string.survey_technical_waste_placement_count),
                    editHint = "",
                    inputFormat = TextFormat.Number(),
                    enabled = false,
                    valueConsumer = StringValueConsumer<TechnicalSurveyDraft>(
                        value = it.toString()
                    ) { _, updatable -> updatable }
                )
            )
            add(EmptySpace(isNested = true))
        }
    }

    private fun MutableList<Any>.addNestingWastePlacementMaps(items: List<WastePlacementMap>) {
        val lastIndex = items.size - 1
        items.forEachIndexed { index, wastePlacementMap ->
            val isLast = index == lastIndex
            add(CardCornersItem(isTop = true, isNested = true))
            add(
                InnerLabeledDateItem(
                    entityId = wastePlacementMap.id,
                    inCard = true,
                    label = resourceManager.getString(
                        R.string.survey_technical_waste_placement_map_commissioning_period_label
                    ),
                    editHint = resourceManager.getString(
                        R.string.survey_technical_waste_placement_map_commissioning_period_hint
                    ),
                    valueConsumer = DateValueConsumer<TechnicalSurveyDraft>(
                        value = wastePlacementMap.commissioningPeriod
                    ) { value, updatable ->
                        val survey = updatable.technicalSurvey as TechnicalSurvey.WastePlacementTechnicalSurvey
                        val newValue = survey.wastePlacementMaps[wastePlacementMap.id]?.copy(
                            commissioningPeriod = value
                        ).requireNotNull()

                        updatable.copy(
                            technicalSurvey = survey.copy(
                                wastePlacementMaps = updatable.technicalSurvey.wastePlacementMaps
                                    .toMutableMap()
                                    .apply {
                                        put(wastePlacementMap.id, newValue)
                                    }
                            )
                        )
                    }
                )
            )
            add(
                InnerLabeledEditItem(
                    entityId = wastePlacementMap.id,
                    inCard = true,
                    label = resourceManager.getString(R.string.survey_technical_waste_placement_map_area_label),
                    editHint = resourceManager.getString(R.string.survey_technical_waste_placement_map_area_hint),
                    inputFormat = TextFormat.NumberDecimal(),
                    valueConsumer = StringValueConsumer<TechnicalSurveyDraft>(
                        value = wastePlacementMap.area?.toString()
                    ) { value, updatable ->
                        val survey = updatable.technicalSurvey as TechnicalSurvey.WastePlacementTechnicalSurvey
                        val newValue = survey.wastePlacementMaps[wastePlacementMap.id]?.copy(
                            area = value?.toFloatOrNull()
                        ).requireNotNull()

                        updatable.copy(
                            technicalSurvey = survey.copy(
                                wastePlacementMaps = updatable.technicalSurvey.wastePlacementMaps
                                    .toMutableMap()
                                    .apply {
                                        put(wastePlacementMap.id, newValue)
                                    }
                            )
                        )
                    }
                )
            )
            add(
                CardEmptyLine(
                    height = resourceManager.getDimensionPixelSize(R.dimen.default_padding),
                    horizontalPadding = resourceManager.getDimensionPixelSize(R.dimen.default_side_padding),
                    isNested = true
                )
            )
            add(
                DeleteEntityItem(
                    id = wastePlacementMap.id,
                    inCard = true,
                    qualifier = TechnicalCardType.WASTE_PLACEMENT_MAP
                )
            )
            add(CardCornersItem(isTop = false, isNested = true))
            EmptySpace(isNested = true).takeUnless { isLast }?.also(::add)
        }
    }
}
