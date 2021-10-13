package ru.ktsstudio.app_verification.presentation.object_survey.equipment.conveyors

import androidx.annotation.StringRes
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentCheckableDataType
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.equipment
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.otherConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.pressConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.reverseConveyors

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class EquipmentConveyorUiMapper(
    private val resourceManager: ResourceManager
) : Mapper<EquipmentSurveyDraft, List<Any>> {

    @OptIn(ExperimentalStdlibApi::class)
    override fun map(item: EquipmentSurveyDraft): List<Any> {
        return buildList {
            add(getTitle(R.string.survey_equipment_equipment_title))
            add(getTitle(R.string.survey_equipment_conveyor_title))
            addServingConveyors(item)
            addSortConveyors(item)
            addReverseConveyors(item)
            addPressConveyors(item)
            addOtherConveyors(item)
            addBagBreakersConveyors(item)
        }
    }

    private fun getTitle(@StringRes titleRes: Int): LargeTitleItem {
        return LargeTitleItem(resourceManager.getString(titleRes))
    }

    private fun getEmpty() = InnerMediumTitle(
        text = resourceManager.getString(R.string.survey_equipment_empty),
        withAccent = false
    )

    private fun getAddButton(type: ConveyorType) =
        AddEntityItem(
            nested = true,
            text = resourceManager.getString(R.string.survey_equipment_add),
            qualifier = EquipmentEntity.Conveyor(type)
        )

    private fun MutableList<Any>.addServingConveyors(item: EquipmentSurveyDraft) {
        add(
            SubtitleItemWithCheck(
                checkableValueConsumer = EquipmentCheckableDataType.ServingConveyor(
                    item.checkedEquipment.servingConveyors
                ),
                title = resourceManager.getString(R.string.survey_equipment_serving_conveyor_title)
            )
        )
        item.takeIf { it.equipment.servingConveyors.isNotEmpty() }
            ?.let { addNestedServingConveyors(item, resourceManager) }
            ?: add(getEmpty())
        add(getAddButton(ConveyorType.SERVING))
    }

    private fun MutableList<Any>.addSortConveyors(item: EquipmentSurveyDraft) {
        add(
            SubtitleItemWithCheck(
                checkableValueConsumer = EquipmentCheckableDataType.SortConveyor(
                    item.checkedEquipment.sortConveyors
                ),
                title = resourceManager.getString(R.string.survey_equipment_sort_conveyor_title)
            )
        )
        item.takeIf { it.equipment.sortConveyors.isNotEmpty() }
            ?.let { addNestedSortConveyors(item, resourceManager) }
            ?: add(getEmpty())
        add(getAddButton(ConveyorType.SORT))
    }

    private fun MutableList<Any>.addReverseConveyors(item: EquipmentSurveyDraft) {
        add(
            SubtitleItemWithCheck(
                checkableValueConsumer = EquipmentCheckableDataType.ReverseConveyor(
                    item.checkedEquipment.reverseConveyors
                ),
                title = resourceManager.getString(R.string.survey_equipment_reverse_conveyor_title)
            )
        )
        item.takeIf { it.equipment.reverseConveyors.isNotEmpty() }
            ?.let {
                addNestedConveyors(
                    conveyors = item.equipment.reverseConveyors.values,
                    conveyorType = ConveyorType.REVERSE,
                    conveyorMapLens = EquipmentSurveyDraft.equipment
                        .reverseConveyors,
                    resourceManager = resourceManager
                )
            }
            ?: add(getEmpty())
        add(getAddButton(ConveyorType.REVERSE))
    }

    private fun MutableList<Any>.addPressConveyors(item: EquipmentSurveyDraft) {
        add(
            SubtitleItemWithCheck(
                checkableValueConsumer = EquipmentCheckableDataType.PressConveyor(
                    item.checkedEquipment.pressConveyors
                ),
                title = resourceManager.getString(R.string.survey_equipment_press_conveyor_title)
            )
        )
        item.takeIf { it.equipment.pressConveyors.isNotEmpty() }
            ?.let {
                addNestedConveyors(
                    conveyors = item.equipment.pressConveyors.values,
                    conveyorType = ConveyorType.PRESS,
                    conveyorMapLens = EquipmentSurveyDraft.equipment
                        .pressConveyors,
                    resourceManager = resourceManager
                )
            }
            ?: add(getEmpty())
        add(getAddButton(ConveyorType.PRESS))
    }

    private fun MutableList<Any>.addOtherConveyors(item: EquipmentSurveyDraft) {
        add(
            SubtitleItemWithCheck(
                checkableValueConsumer = EquipmentCheckableDataType.OtherConveyor(
                    item.checkedEquipment.otherConveyors
                ),
                title = resourceManager.getString(R.string.survey_equipment_other_conveyor_title)
            )
        )
        item.takeIf { it.equipment.otherConveyors.isNotEmpty() }
            ?.let {
                addNestedConveyors(
                    conveyors = item.equipment.otherConveyors.values,
                    conveyorType = ConveyorType.OTHER,
                    conveyorMapLens = EquipmentSurveyDraft.equipment
                        .otherConveyors,
                    resourceManager = resourceManager
                )
            }
            ?: add(getEmpty())
        add(getAddButton(ConveyorType.OTHER))
    }

    private fun MutableList<Any>.addBagBreakersConveyors(item: EquipmentSurveyDraft) {
        add(
            SubtitleItemWithCheck(
                checkableValueConsumer = EquipmentCheckableDataType.BagBreakerConveyor(
                    item.checkedEquipment.bagBreakers
                ),
                title = resourceManager.getString(R.string.survey_equipment_back_breaker_conveyor_title)
            )
        )
        item.takeIf { it.equipment.bagBreakers.isNotEmpty() }
            ?.let { addNestedBagBreakerConveyors(item, resourceManager) }
            ?: add(getEmpty())
        add(getAddButton(ConveyorType.BAG_BREAKER))
    }
}
