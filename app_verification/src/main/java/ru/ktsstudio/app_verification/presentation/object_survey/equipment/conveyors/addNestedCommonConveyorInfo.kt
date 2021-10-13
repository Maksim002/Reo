package ru.ktsstudio.app_verification.presentation.object_survey.equipment.conveyors

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.stringValue
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonConveyorInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableLength
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableSpeed
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableWidth

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */

fun MutableList<Any>.addNestedCommonConveyorInfo(
    conveyorId: String,
    info: CommonConveyorInfo,
    updaterOptics: Optional<EquipmentSurveyDraft, CommonConveyorInfo>,
    resourceManager: ResourceManager
) {
    listOf(
        InnerLabeledEditItem(
            entityId = conveyorId,
            inCard = true,
            label = resourceManager.getString(R.string.survey_equipment_label_conveyor_length),
            editHint = resourceManager.getString(R.string.survey_equipment_hint_conveyor_length),
            inputFormat = TextFormat.NumberDecimal(decimalDigits = 2),
            valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                value = info.length.stringValue()
            ) { value, updatable ->
                updaterOptics
                    .nullableLength
                    .set(updatable, value?.floatValue())
            }
        ),
        InnerLabeledEditItem(
            entityId = conveyorId,
            inCard = true,
            label = resourceManager.getString(R.string.survey_equipment_label_conveyor_width),
            editHint = resourceManager.getString(R.string.survey_equipment_hint_conveyor_width),
            inputFormat = TextFormat.NumberDecimal(decimalDigits = 2),
            valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                value = info.width.stringValue()
            ) { value, updatable ->
                updaterOptics
                    .nullableWidth
                    .set(updatable, value?.floatValue())
            }
        ),
        InnerLabeledEditItem(
            entityId = conveyorId,
            inCard = true,
            label = resourceManager.getString(R.string.survey_equipment_label_conveyor_speed),
            editHint = resourceManager.getString(R.string.survey_equipment_hint_conveyor_speed),
            inputFormat = TextFormat.NumberDecimal(decimalDigits = 2),
            valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                value = info.speed.stringValue()
            ) { value, updatable ->
                updaterOptics
                    .nullableSpeed
                    .set(updatable, value?.floatValue())
            }
        )
    ).let { addAll(it) }
}
