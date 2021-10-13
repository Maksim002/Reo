package ru.ktsstudio.app_verification.presentation.object_survey.equipment.separators

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
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Separator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableOtherName
import ru.ktsstudio.core_data_verfication_api.data.model.separators
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class EquipmentSeparatorUiMapper(
    private val resourceManager: ResourceManager
) : Mapper<EquipmentSurveyDraft, List<Any>> {
    @OptIn(ExperimentalStdlibApi::class)
    override fun map(item: EquipmentSurveyDraft): List<Any> {
        return buildList {
            add(getTitle())
            val separatorTypeToSeparatorsMap = item.equipment.separators.values.groupBy { it.type }
            SeparatorType.values().forEach { separatorType ->
                val separatorsForType = separatorTypeToSeparatorsMap[separatorType].orEmpty()
                val checked = item.checkedEquipment.separators.contains(separatorType)
                add(
                    SubtitleItemWithCheck(
                        checkableValueConsumer = EquipmentCheckableDataType.Separator(
                            checked,
                            separatorType
                        ),
                        title = resourceManager.getString(separatorType.getTitle())
                    )
                )

                separatorsForType.takeIfNotEmpty()
                    ?.also { addSeparatorsByType(separatorType, separatorsForType) }
                    ?: add(getEmpty())

                add(
                    AddEntityItem(
                        nested = true,
                        text = resourceManager.getString(R.string.survey_equipment_add),
                        qualifier = EquipmentEntity.Separator(separatorType)
                    )
                )
            }
        }
    }

    private fun getTitle() =
        LargeTitleItem(resourceManager.getString(R.string.survey_equipment_separator_title))

    private fun getEmpty() = InnerMediumTitle(
        text = resourceManager.getString(R.string.survey_equipment_empty),
        withAccent = false
    )

    private fun MutableList<Any>.addSeparatorsByType(
        type: SeparatorType,
        separators: List<Separator>
    ) {
        separators.forEachIndexed { index, separator ->
            val isLast = index == separators.lastIndex
            add(CardCornersItem(isTop = true, isNested = true))
            if (separator.type == SeparatorType.OTHER) {
                add(
                    InnerLabeledEditItem(
                        entityId = separator.id,
                        inputFormat = TextFormat.Text,
                        label = resourceManager.getString(R.string.survey_equipment_label_separator_other_name),
                        editHint = resourceManager.getString(R.string.survey_name_hint),
                        inCard = true,
                        valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                            value = separator.otherName
                        ) { value, updatable ->
                            EquipmentSurveyDraft.equipment.separators
                                .at(MapK.at(), separator.id)
                                .some
                                .nullableOtherName
                                .set(updatable, value)
                        }
                    )
                )
            }
            addNestedCommonEquipmentInfo(
                equipmentId = separator.id,
                info = separator.commonEquipmentInfo,
                updaterOptics = EquipmentSurveyDraft.equipment.separators
                    .at(MapK.at(), separator.id)
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
                    id = separator.id,
                    inCard = true,
                    qualifier = EquipmentEntity.Separator(type)
                )
            )
            add(CardCornersItem(isTop = false, isNested = true))
            EmptySpace(isNested = true).takeUnless { isLast }?.also(::add)
        }
    }

    private fun SeparatorType.getTitle(): Int {
        return when (this) {
            SeparatorType.BALLISTIC -> R.string.survey_equipment_separator_ballistic_title
            SeparatorType.EDDY_CURRENT -> R.string.survey_equipment_separator_eddy_current_title
            SeparatorType.FRACTION -> R.string.survey_equipment_separator_fraction_title
            SeparatorType.IRON -> R.string.survey_equipment_separator_iron_title
            SeparatorType.METAL -> R.string.survey_equipment_separator_metal_title
            SeparatorType.OPTIC -> R.string.survey_equipment_separator_optic_title
            SeparatorType.OTHER -> R.string.survey_equipment_separator_other_title
            SeparatorType.PLASTIC -> R.string.survey_equipment_separator_plastic_title
            SeparatorType.VIBRATION -> R.string.survey_equipment_separator_vibration_title
        }
    }
}
