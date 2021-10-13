package ru.ktsstudio.app_verification.domain.object_survey.equipment

import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectAdder
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.equipment
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.core_data_verfication_api.data.model.additionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.bagBreakers
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.BagBreakerConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Conveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Press
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Separator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ServingConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SortConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.otherConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.pressConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.presses
import ru.ktsstudio.core_data_verfication_api.data.model.reverseConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.separators
import ru.ktsstudio.core_data_verfication_api.data.model.servingConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.sortConveyors
import java.util.UUID

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class EquipmentAdder : NestedObjectAdder<EquipmentSurveyDraft, EquipmentEntity> {
    override fun invoke(draft: EquipmentSurveyDraft, entityType: EquipmentEntity): EquipmentSurveyDraft {
        val id = generateId(entityType)
        return when (entityType) {
            is EquipmentEntity.Conveyor -> draft.addNewConveyor(id, entityType.type)
            is EquipmentEntity.Separator -> draft.addNewSeparator(id, entityType.type)
            is EquipmentEntity.Press -> draft.addNewPress(id, entityType.type)
            is EquipmentEntity.Additional -> draft.addNewAdditional(id)
        }
    }

    private fun generateId(entityType: EquipmentEntity): String {
        return UUID.randomUUID().toString()
    }

    private fun EquipmentSurveyDraft.addNewConveyor(id: String, type: ConveyorType): EquipmentSurveyDraft {
        return when (type) {
            ConveyorType.SERVING -> EquipmentSurveyDraft.equipment.servingConveyors.modify(this) {
                it.plus(id to ServingConveyor.createEmpty(id = id)).k()
            }
            ConveyorType.SORT -> EquipmentSurveyDraft.equipment.sortConveyors.modify(this) {
                it.plus(id to SortConveyor.createEmpty(id = id)).k()
            }
            ConveyorType.REVERSE -> EquipmentSurveyDraft.equipment.reverseConveyors.modify(this) {
                it.plus(id to Conveyor.createEmpty(id = id)).k()
            }
            ConveyorType.PRESS -> EquipmentSurveyDraft.equipment.pressConveyors.modify(this) {
                it.plus(id to Conveyor.createEmpty(id = id)).k()
            }
            ConveyorType.OTHER -> EquipmentSurveyDraft.equipment.otherConveyors.modify(this) {
                it.plus(id to Conveyor.createEmpty(id = id)).k()
            }
            ConveyorType.BAG_BREAKER -> EquipmentSurveyDraft.equipment.bagBreakers.modify(this) {
                it.plus(id to BagBreakerConveyor.createEmpty(id = id)).k()
            }
            else -> this
        }
    }

    private fun EquipmentSurveyDraft.addNewSeparator(id: String, type: SeparatorType): EquipmentSurveyDraft {
        return EquipmentSurveyDraft.equipment.separators.modify(this) {
            it.plus(id to Separator.createEmpty(id = id, type = type)).k()
        }
    }

    private fun EquipmentSurveyDraft.addNewPress(id: String, type: PressType): EquipmentSurveyDraft {
        return EquipmentSurveyDraft.equipment.presses.modify(this) {
            it.plus(id to Press.createEmpty(id = id, type = type)).k()
        }
    }

    private fun EquipmentSurveyDraft.addNewAdditional(id: String): EquipmentSurveyDraft {
        return EquipmentSurveyDraft.equipment.additionalEquipment.modify(this) {
            it.plus(id to AdditionalEquipment.createEmpty(id = id)).k()
        }
    }
}
