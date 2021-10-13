package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.core.MapK
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.WeightPlatformLengthUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.infrastructureSurvey
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.addNestedCommonEquipmentInfo
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.commonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.equipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableCount
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullableWeightPlatformLength
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.weightControl

fun MutableList<Any>.addNestedWeightControlEquipment(
    item: WeightControl,
    resources: ResourceManager
) {
    val lastIndex = item.equipment.values.size - 1
    add(
        InnerMediumTitle(
            text = resources.getString(R.string.survey_infrastructure_equipment_label),
            identifier = WeightControl::class.java.canonicalName
        )
    )
    add(EmptySpace(isNested = true))
    add(
        InnerLabeledEditItem(
            label = resources.getString(R.string.survey_equipment_label_count),
            editHint = resources.getString(R.string.survey_equipment_count_hint),
            inputFormat = TextFormat.Number(),
            valueConsumer = StringValueConsumer<InfrastructureSurveyDraft>(
                value = item.count?.toString().orEmpty()
            ) { value, updatable ->
                InfrastructureSurveyDraft.infrastructureSurvey
                    .weightControl
                    .nullableCount
                    .set(updatable, value?.toIntOrNull())
            }
        )
    )
    add(EmptySpace(isNested = true))
    item.equipment.values.forEachIndexed { index, equipment ->
        val isLast = index == lastIndex
        add(CardCornersItem(isTop = true, isNested = true))
        addNestedCommonEquipmentInfo(
            equipmentId = equipment.id,
            info = equipment.commonEquipmentInfo,
            updaterOptics = InfrastructureSurveyDraft.infrastructureSurvey
                .weightControl
                .equipment
                .at(MapK.at(), equipment.id)
                .some
                .commonEquipmentInfo,
            resourceManager = resources,
            withEquipmentCount = false
        )
        add(
            InnerLabeledEditItem(
                label = resources.getString(R.string.survey_infrastructure_platform_length_label),
                editHint = resources.getString(R.string.survey_infrastructure_platform_length_hint),
                inputFormat = TextFormat.Number(),
                valueConsumer = WeightPlatformLengthUpdater(
                    length = equipment.weightPlatformLength,
                    weightPlatformOptics = InfrastructureSurveyDraft.infrastructureSurvey
                        .weightControl
                        .equipment
                        .at(MapK.at(), equipment.id)
                        .some
                        .nullableWeightPlatformLength
                ),
                entityId = WeightControl::class.java.canonicalName,
                inCard = true
            )
        )
        add(
            CardEmptyLine(
                height = resources.getDimensionPixelSize(R.dimen.default_padding),
                horizontalPadding = resources.getDimensionPixelSize(R.dimen.default_side_padding),
                isNested = true
            )
        )
        add(
            DeleteEntityItem(
                id = equipment.id,
                inCard = true,
                qualifier = InfrastructureEquipmentType.WEIGHT_CONTROL
            )
        )
        add(CardCornersItem(isTop = false, isNested = true))

        if (isLast.not()) add(EmptySpace(isNested = true))
    }
}
