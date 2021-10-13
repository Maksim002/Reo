package ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator

import ru.ktsstudio.app_verification.domain.object_survey.common.SurveyDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.BagBreakerConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Conveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Press
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Separator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ServingConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SortConveyor

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.12.2020.
 */
class EquipmentSurveyDraftValidator(
    private val servingConveyorFillValidator: FillValidator<ServingConveyor>,
    private val sortConveyorFillValidator: FillValidator<SortConveyor>,
    private val conveyorFillValidator: FillValidator<Conveyor>,
    private val bagBreakerFillValidator: FillValidator<BagBreakerConveyor>,
    private val separatorFillValidator: FillValidator<Separator>,
    private val pressFillValidator: FillValidator<Press>,
    private val additionalFillValidator: FillValidator<AdditionalEquipment>
) : SurveyDraftValidator<EquipmentSurveyDraft> {
    override fun invoke(draft: EquipmentSurveyDraft): Boolean = with(draft.equipment) {
        return servingConveyors.values.all(servingConveyorFillValidator::isFilled) &&
            sortConveyors.values.all(sortConveyorFillValidator::isFilled) &&
            reverseConveyors.values.all(conveyorFillValidator::isFilled) &&
            pressConveyors.values.all(conveyorFillValidator::isFilled) &&
            otherConveyors.values.all(conveyorFillValidator::isFilled) &&
            bagBreakers.values.all(bagBreakerFillValidator::isFilled) &&
            separators.values.all(separatorFillValidator::isFilled) &&
            presses.values.all(pressFillValidator::isFilled) &&
            additionalEquipment.values.all(additionalFillValidator::isFilled)
    }
}
