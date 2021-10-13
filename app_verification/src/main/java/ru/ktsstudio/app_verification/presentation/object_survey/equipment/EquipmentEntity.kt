package ru.ktsstudio.app_verification.presentation.object_survey.equipment

import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.12.2020.
 */
sealed class EquipmentEntity {
    data class Conveyor(val type: ConveyorType) : EquipmentEntity()
    data class Press(val type: PressType) : EquipmentEntity()
    data class Separator(val type: SeparatorType) : EquipmentEntity()
    object Additional : EquipmentEntity()
}
