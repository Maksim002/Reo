package ru.ktsstudio.app_verification.presentation.object_survey.equipment.conveyors

import arrow.core.MapK
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.equipment
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.addNestedCommonEquipmentInfo
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.stringValue
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonConveyorInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableSortPointCount
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableWasteHeight
import ru.ktsstudio.core_data_verfication_api.data.model.sortConveyors

fun MutableList<Any>.addNestedSortConveyors(item: EquipmentSurveyDraft, resourceManager: ResourceManager) {
    val lastIndex = item.equipment.sortConveyors.values.size - 1
    item.equipment.sortConveyors.values.forEachIndexed { index, conveyor ->
        val isLast = index == lastIndex
        add(CardCornersItem(isTop = true, isNested = true))
        addNestedCommonEquipmentInfo(
            equipmentId = conveyor.id,
            info = conveyor.commonEquipmentInfo,
            updaterOptics = EquipmentSurveyDraft.equipment
                .sortConveyors
                .at(MapK.at(), conveyor.id)
                .some
                .commonEquipmentInfo,
            resourceManager = resourceManager
        )
        addNestedCommonConveyorInfo(
            conveyorId = conveyor.id,
            info = conveyor.commonConveyorInfo,
            updaterOptics = EquipmentSurveyDraft.equipment
                .sortConveyors
                .at(MapK.at(), conveyor.id)
                .some
                .commonConveyorInfo,
            resourceManager = resourceManager
        )
        add(
            InnerLabeledEditItem(
                entityId = conveyor.id,
                inCard = true,
                label = resourceManager.getString(R.string.survey_equipment_label_conveyor_waste_height),
                editHint = resourceManager.getString(R.string.survey_equipment_hint_conveyor_waste_height),
                inputFormat = TextFormat.Number(),
                valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                    value = conveyor.wasteHeight?.stringValue()
                ) { value, updatable ->
                    EquipmentSurveyDraft.equipment
                        .sortConveyors
                        .at(MapK.at(), conveyor.id)
                        .some
                        .nullableWasteHeight
                        .set(updatable, value?.floatValue())
                }
            )
        )

        add(
            InnerLabeledEditItem(
                entityId = conveyor.id,
                inCard = true,
                label = resourceManager.getString(R.string.survey_equipment_label_conveyor_sort_point),
                editHint = resourceManager.getString(R.string.survey_equipment_hint_conveyor_waste_sort_point),
                inputFormat = TextFormat.Number(),
                valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                    value = conveyor.sortPointCount?.toString().orEmpty()
                ) { value, updatable ->
                    EquipmentSurveyDraft.equipment
                        .sortConveyors
                        .at(MapK.at(), conveyor.id)
                        .some
                        .nullableSortPointCount
                        .set(updatable, value?.toIntOrNull())
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
                id = conveyor.id,
                inCard = true,
                qualifier = EquipmentEntity.Conveyor(ConveyorType.SORT)
            )
        )
        add(CardCornersItem(isTop = false, isNested = true))
        EmptySpace(isNested = true).takeUnless { isLast }?.also(::add)
    }
}
