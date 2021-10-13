package ru.ktsstudio.app_verification.domain.object_survey.equipment.models

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.common.utils.toggleItem
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.12.2020.
 */
sealed class EquipmentCheckableDataType : CheckableValueConsumer<Unit, EquipmentSurveyDraft> {

    override fun get(): Unit = Unit
    override fun consume(value: Unit): Updater<EquipmentSurveyDraft> = this

    data class ServingConveyor(override val isChecked: Boolean) : EquipmentCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<EquipmentSurveyDraft> = copy(isChecked = isChecked)

        override fun update(updatable: EquipmentSurveyDraft): EquipmentSurveyDraft {
            return updatable.copy(
                checkedEquipment = updatable.checkedEquipment.copy(
                    servingConveyors = isChecked
                )
            )
        }
    }

    data class SortConveyor(override val isChecked: Boolean) : EquipmentCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<EquipmentSurveyDraft> = copy(isChecked = isChecked)

        override fun update(updatable: EquipmentSurveyDraft): EquipmentSurveyDraft {
            return updatable.copy(
                checkedEquipment = updatable.checkedEquipment.copy(
                    sortConveyors = isChecked
                )
            )
        }
    }

    data class ReverseConveyor(override val isChecked: Boolean) : EquipmentCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<EquipmentSurveyDraft> = copy(isChecked = isChecked)

        override fun update(updatable: EquipmentSurveyDraft): EquipmentSurveyDraft {
            return updatable.copy(
                checkedEquipment = updatable.checkedEquipment.copy(
                    reverseConveyors = isChecked
                )
            )
        }
    }

    data class PressConveyor(override val isChecked: Boolean) : EquipmentCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<EquipmentSurveyDraft> = copy(isChecked = isChecked)

        override fun update(updatable: EquipmentSurveyDraft): EquipmentSurveyDraft {
            return updatable.copy(
                checkedEquipment = updatable.checkedEquipment.copy(
                    pressConveyors = isChecked
                )
            )
        }
    }

    data class OtherConveyor(override val isChecked: Boolean) : EquipmentCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<EquipmentSurveyDraft> = copy(isChecked = isChecked)

        override fun update(updatable: EquipmentSurveyDraft): EquipmentSurveyDraft {
            return updatable.copy(
                checkedEquipment = updatable.checkedEquipment.copy(
                    otherConveyors = isChecked
                )
            )
        }
    }

    data class BagBreakerConveyor(override val isChecked: Boolean) : EquipmentCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<EquipmentSurveyDraft> = copy(isChecked = isChecked)

        override fun update(updatable: EquipmentSurveyDraft): EquipmentSurveyDraft {
            return updatable.copy(
                checkedEquipment = updatable.checkedEquipment.copy(
                    bagBreakers = isChecked
                )
            )
        }
    }

    data class Additional(override val isChecked: Boolean) : EquipmentCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<EquipmentSurveyDraft> {
            return copy(isChecked = isChecked)
        }

        override fun update(updatable: EquipmentSurveyDraft): EquipmentSurveyDraft {
            return updatable.copy(
                checkedEquipment = updatable.checkedEquipment.copy(
                    additionalEquipment = isChecked
                )
            )
        }
    }

    data class Separator(
        override val isChecked: Boolean,
        private val type: SeparatorType
    ) : EquipmentCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<EquipmentSurveyDraft> = copy(isChecked = isChecked)

        override fun update(updatable: EquipmentSurveyDraft): EquipmentSurveyDraft {
            return updatable.copy(
                checkedEquipment = updatable.checkedEquipment.copy(
                    separators = updatable.checkedEquipment.separators.toggleItem(type)
                )
            )
        }
    }

    data class Press(
        override val isChecked: Boolean,
        private val type: PressType
    ) : EquipmentCheckableDataType() {
        override fun setChecked(isChecked: Boolean): Updater<EquipmentSurveyDraft> = copy(isChecked = isChecked)

        override fun update(updatable: EquipmentSurveyDraft): EquipmentSurveyDraft {
            return updatable.copy(
                checkedEquipment = updatable.checkedEquipment.copy(
                    presses = updatable.checkedEquipment.presses.toggleItem(type)
                )
            )
        }
    }
}
