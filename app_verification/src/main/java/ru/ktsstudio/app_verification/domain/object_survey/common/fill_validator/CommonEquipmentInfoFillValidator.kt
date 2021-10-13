package ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator

import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.12.2020.
 */
class CommonEquipmentInfoFillValidator @Inject constructor() : FillValidator<CommonEquipmentInfo> {
    override fun isFilled(entity: CommonEquipmentInfo): Boolean {
        return entity.brand.isNotBlank() &&
            entity.manufacturer.isNotBlank() &&
            entity.count != null &&
            entity.commonMediaInfo.isFilled
    }
}
