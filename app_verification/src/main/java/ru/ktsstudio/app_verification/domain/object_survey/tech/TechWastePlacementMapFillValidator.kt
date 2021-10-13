package ru.ktsstudio.app_verification.domain.object_survey.tech

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 26.01.2021.
 */
class TechWastePlacementMapFillValidator @Inject constructor() : FillValidator<WastePlacementMap> {
    override fun isFilled(entity: WastePlacementMap): Boolean {
        return entity.area != null &&
            entity.commissioningPeriod != null
    }
}
