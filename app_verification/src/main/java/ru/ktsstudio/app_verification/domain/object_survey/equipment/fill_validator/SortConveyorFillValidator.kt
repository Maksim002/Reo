package ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonConveyorInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SortConveyor
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.12.2020.
 */
class SortConveyorFillValidator @Inject constructor(
    private val commonEquipmentInfoFillValidator: FillValidator<CommonEquipmentInfo>,
    private val commonConveyorInfoFillValidator: FillValidator<CommonConveyorInfo>,
) : FillValidator<SortConveyor> {
    override fun isFilled(entity: SortConveyor): Boolean {
        return entity.id.isNotEmpty() &&
            entity.wasteHeight != null &&
            entity.sortPointCount != null &&
            commonEquipmentInfoFillValidator.isFilled(entity.commonEquipmentInfo) &&
            commonConveyorInfoFillValidator.isFilled(entity.commonConveyorInfo)
    }
}
