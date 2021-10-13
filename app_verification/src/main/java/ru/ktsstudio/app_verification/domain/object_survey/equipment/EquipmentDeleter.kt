package ru.ktsstudio.app_verification.domain.object_survey.equipment

import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectDeleter
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.equipment
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.core_data_verfication_api.data.model.additionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.bagBreakers
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.otherConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.pressConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.presses
import ru.ktsstudio.core_data_verfication_api.data.model.reverseConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.separators
import ru.ktsstudio.core_data_verfication_api.data.model.servingConveyors
import ru.ktsstudio.core_data_verfication_api.data.model.sortConveyors

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class EquipmentDeleter : NestedObjectDeleter<EquipmentSurveyDraft, EquipmentEntity> {

    override fun invoke(draft: EquipmentSurveyDraft, id: String, entityType: EquipmentEntity): EquipmentSurveyDraft {
        return when (entityType) {
            is EquipmentEntity.Conveyor -> draft.deleteConveyor(id, entityType.type)
            is EquipmentEntity.Separator -> draft.deleteSeparator(id)
            is EquipmentEntity.Press -> draft.deletePress(id)
            is EquipmentEntity.Additional -> draft.deleteAdditionalEquipment(id)
        }
    }

    private fun EquipmentSurveyDraft.deleteConveyor(id: String, type: ConveyorType): EquipmentSurveyDraft {
        return when (type) {
            ConveyorType.SERVING -> EquipmentSurveyDraft.equipment.servingConveyors.modify(this) {
                it.minus(id).k()
            }
            ConveyorType.SORT -> EquipmentSurveyDraft.equipment.sortConveyors.modify(this) {
                it.minus(id).k()
            }
            ConveyorType.REVERSE -> EquipmentSurveyDraft.equipment.reverseConveyors.modify(this) {
                it.minus(id).k()
            }
            ConveyorType.PRESS -> EquipmentSurveyDraft.equipment.pressConveyors.modify(this) {
                it.minus(id).k()
            }
            ConveyorType.OTHER -> EquipmentSurveyDraft.equipment.otherConveyors.modify(this) {
                it.minus(id).k()
            }
            ConveyorType.BAG_BREAKER -> EquipmentSurveyDraft.equipment.bagBreakers.modify(this) {
                it.minus(id).k()
            }
            else -> this
        }
    }

    private fun EquipmentSurveyDraft.deleteSeparator(id: String): EquipmentSurveyDraft {
        return EquipmentSurveyDraft.equipment.separators.modify(this) {
            it.minus(id).k()
        }
    }

    private fun EquipmentSurveyDraft.deletePress(id: String): EquipmentSurveyDraft {
        return EquipmentSurveyDraft.equipment.presses.modify(this) {
            it.minus(id).k()
        }
    }

    private fun EquipmentSurveyDraft.deleteAdditionalEquipment(id: String): EquipmentSurveyDraft {
        return EquipmentSurveyDraft.equipment.additionalEquipment.modify(this) {
            it.minus(id).k()
        }
    }
}
