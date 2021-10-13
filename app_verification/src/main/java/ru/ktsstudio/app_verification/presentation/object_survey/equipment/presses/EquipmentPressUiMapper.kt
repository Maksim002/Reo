package ru.ktsstudio.app_verification.presentation.object_survey.equipment.presses

import arrow.core.MapK
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentCheckableDataType
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.equipment
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.addNestedCommonEquipmentInfo
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Press
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableOtherName
import ru.ktsstudio.core_data_verfication_api.data.model.presses
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class EquipmentPressUiMapper(
    private val resourceManager: ResourceManager
) : Mapper<EquipmentSurveyDraft, List<Any>> {
    @OptIn(ExperimentalStdlibApi::class)
    override fun map(item: EquipmentSurveyDraft): List<Any> {
        return buildList {
            add(getTitle())
            val pressTypeToPressesMap = item.equipment.presses.values.groupBy { it.type }
            PressType.values().forEach { pressType ->
                val pressesForType = pressTypeToPressesMap[pressType].orEmpty()
                val checked = item.checkedEquipment.presses.contains(pressType)
                add(
                    SubtitleItemWithCheck(
                        checkableValueConsumer = EquipmentCheckableDataType.Press(
                            checked,
                            pressType
                        ),
                        title = resourceManager.getString(pressType.getTitle())
                    )
                )

                pressesForType.takeIfNotEmpty()
                    ?.also { addPressesByType(pressType, pressesForType) }
                    ?: add(getEmpty())

                add(
                    AddEntityItem(
                        nested = true,
                        text = resourceManager.getString(R.string.survey_equipment_add),
                        qualifier = EquipmentEntity.Press(pressType)
                    )
                )
            }
        }
    }

    private fun getTitle() = LargeTitleItem(resourceManager.getString(R.string.survey_equipment_press_title))

    private fun getEmpty() = InnerMediumTitle(
        text = resourceManager.getString(R.string.survey_equipment_empty),
        withAccent = false
    )

    private fun MutableList<Any>.addPressesByType(type: PressType, presses: List<Press>) {
        presses.forEachIndexed { index, press ->
            val isLast = index == presses.lastIndex
            add(CardCornersItem(isTop = true, isNested = true))
            if (press.type == PressType.OTHER) {
                add(
                    InnerLabeledEditItem(
                        entityId = press.id,
                        inputFormat = TextFormat.Text,
                        label = resourceManager.getString(R.string.survey_equipment_label_press_other_name),
                        editHint = resourceManager.getString(R.string.survey_name_hint),
                        inCard = true,
                        valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                            value = press.otherName
                        ) { value, updatable ->
                            EquipmentSurveyDraft.equipment.presses
                                .at(MapK.at(), press.id)
                                .some
                                .nullableOtherName
                                .set(updatable, value)
                        }
                    )
                )
            }
            addNestedCommonEquipmentInfo(
                equipmentId = press.id,
                info = press.commonEquipmentInfo,
                updaterOptics = EquipmentSurveyDraft.equipment.presses
                    .at(MapK.at(), press.id)
                    .some
                    .commonEquipmentInfo,
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
                    id = press.id,
                    inCard = true,
                    qualifier = EquipmentEntity.Press(type)
                )
            )
            add(CardCornersItem(isTop = false, isNested = true))
            EmptySpace(isNested = true).takeUnless { isLast }?.also(::add)
        }
    }

    private fun PressType.getTitle(): Int {
        return when (this) {
            PressType.COMPACTOR -> R.string.survey_equipment_press_compactor_title
            PressType.HORIZONTAL -> R.string.survey_equipment_press_horizontal_title
            PressType.VERTICAL -> R.string.survey_equipment_press_vertical_title
            PressType.OTHER -> R.string.survey_equipment_press_other_title
            PressType.PACKING_MACHINES -> R.string.survey_equipment_press_packing_machines_title
        }
    }
}
