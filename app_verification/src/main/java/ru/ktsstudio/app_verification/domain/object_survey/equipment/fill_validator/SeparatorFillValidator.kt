package ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Separator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.12.2020.
 */
class SeparatorFillValidator @Inject constructor(
    private val commonEquipmentInfoFillValidator: FillValidator<CommonEquipmentInfo>
) : FillValidator<Separator> {
    override fun isFilled(entity: Separator): Boolean {
        return entity.id.isNotEmpty() &&
            commonEquipmentInfoFillValidator.isFilled(entity.commonEquipmentInfo) &&
            (entity.type != SeparatorType.OTHER || entity.otherName.isNullOrBlank().not())
    }
}
