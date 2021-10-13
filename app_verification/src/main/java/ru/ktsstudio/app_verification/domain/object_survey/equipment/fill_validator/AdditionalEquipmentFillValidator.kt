package ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import javax.inject.Inject

class AdditionalEquipmentFillValidator @Inject constructor(
    private val commonEquipmentInfoFillValidator: FillValidator<CommonEquipmentInfo>
) : FillValidator<AdditionalEquipment> {
    override fun isFilled(entity: AdditionalEquipment): Boolean {
        return entity.id.isNotEmpty() &&
            commonEquipmentInfoFillValidator.isFilled(entity.commonEquipmentInfo) &&
            (entity.type?.type != ReferenceType.OTHER || entity.otherName.isNullOrBlank().not())
    }
}
