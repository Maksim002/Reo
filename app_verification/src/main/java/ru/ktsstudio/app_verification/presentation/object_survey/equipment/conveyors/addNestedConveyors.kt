package ru.ktsstudio.app_verification.presentation.object_survey.equipment.conveyors

import arrow.core.MapK
import arrow.optics.Lens
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.addNestedCommonEquipmentInfo
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Conveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonConveyorInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableOtherConveyorName

fun MutableList<Any>.addNestedConveyors(
    conveyors: Collection<Conveyor>,
    conveyorType: ConveyorType,
    conveyorMapLens: Lens<EquipmentSurveyDraft, MapK<String, Conveyor>>,
    resourceManager: ResourceManager
) {
    val lastIndex = conveyors.size - 1
    conveyors.forEachIndexed { index, conveyor ->
        val isLast = index == lastIndex
        add(CardCornersItem(isTop = true, isNested = true))
        if (conveyorType == ConveyorType.OTHER) {
            add(
                InnerLabeledEditItem(
                    entityId = conveyor.id,
                    inputFormat = TextFormat.Text,
                    label = resourceManager.getString(R.string.survey_equipment_label_conveyor_other_name),
                    editHint = resourceManager.getString(R.string.survey_name_hint),
                    inCard = true,
                    valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                        value = conveyor.otherConveyorName
                    ) { value, updatable ->
                        conveyorMapLens
                            .at(MapK.at(), conveyor.id)
                            .some
                            .nullableOtherConveyorName
                            .set(updatable, value)
                    }
                )
            )
        }
        addNestedCommonEquipmentInfo(
            equipmentId = conveyor.id,
            info = conveyor.commonEquipmentInfo,
            updaterOptics = conveyorMapLens
                .at(MapK.at(), conveyor.id)
                .some
                .commonEquipmentInfo,
            resourceManager = resourceManager
        )
        addNestedCommonConveyorInfo(
            conveyorId = conveyor.id,
            info = conveyor.commonConveyorInfo,
            updaterOptics = conveyorMapLens
                .at(MapK.at(), conveyor.id)
                .some
                .commonConveyorInfo,
            resourceManager = resourceManager
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
                qualifier = EquipmentEntity.Conveyor(conveyorType)
            )
        )
        add(CardCornersItem(isTop = false, isNested = true))
        EmptySpace(isNested = true).takeUnless { isLast }?.also(::add)
    }
}
