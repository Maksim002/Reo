package ru.ktsstudio.app_verification.domain.object_survey.tech

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 21.01.2021.
 */
class TechEquipmentFillValidator @Inject constructor() : FillValidator<TechnicalEquipment> {
    override fun isFilled(entity: TechnicalEquipment): Boolean {
        return entity.commonEquipmentInfo.brand.isNotBlank() &&
            entity.commonEquipmentInfo.manufacturer.isNotBlank() &&
            entity.commonEquipmentInfo.commonMediaInfo.isFilled &&
            entity.power != null &&
            entity.type != null &&
            entity.description.isNotBlank() &&
            entity.production.isNotBlank() &&
            entity.count != null
    }
}
