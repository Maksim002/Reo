package ru.ktsstudio.app_verification.domain.object_survey.equipment.fill_validator

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonConveyorInfo
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.12.2020.
 */
class CommonConveyorInfoFillValidator @Inject constructor() : FillValidator<CommonConveyorInfo> {
    override fun isFilled(entity: CommonConveyorInfo): Boolean {
        return entity.length != null &&
            entity.width != null &&
            entity.speed != null
    }
}
