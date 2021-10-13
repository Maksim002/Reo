package ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.BagBreakerConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonConveyorInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.12.2020.
 */
class BagBreakerFillValidator @Inject constructor(
    private val commonEquipmentInfoFillValidator: FillValidator<CommonEquipmentInfo>,
    private val commonConveyorInfoFillValidator: FillValidator<CommonConveyorInfo>,
) : FillValidator<BagBreakerConveyor> {
    override fun isFilled(entity: BagBreakerConveyor): Boolean {
        return entity.id.isNotEmpty() &&
            commonEquipmentInfoFillValidator.isFilled(entity.commonEquipmentInfo)
    }
}
