package ru.ktsstudio.app_verification.domain.object_survey.secondary_resources

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourceType
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.12.2020.
 */
class SecondaryResourceTypeFillValidator @Inject constructor() : FillValidator<SecondaryResourceType> {
    override fun isFilled(entity: SecondaryResourceType): Boolean {
        return entity.id.isNotEmpty() &&
            entity.percent != null &&
            entity.reference != null
    }
}
