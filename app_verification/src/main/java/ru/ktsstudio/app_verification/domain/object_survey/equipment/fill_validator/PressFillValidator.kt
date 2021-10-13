package ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Press
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.12.2020.
 */
class PressFillValidator @Inject constructor(
    private val commonEquipmentInfoFillValidator: FillValidator<CommonEquipmentInfo>
) : FillValidator<Press> {
    override fun isFilled(entity: Press): Boolean {
        return entity.id.isNotEmpty() &&
            commonEquipmentInfoFillValidator.isFilled(entity.commonEquipmentInfo) &&
            (entity.type != PressType.OTHER || entity.otherName.isNullOrBlank().not())
    }
}
