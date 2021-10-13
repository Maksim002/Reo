package ru.ktsstudio.app_verification.domain.object_survey.product.fill_validator

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.production.Service
import javax.inject.Inject

/**
 * Created by Igor Park on 17/12/2020.
 */
class ServiceFillValidator @Inject constructor() : FillValidator<Service> {
    override fun isFilled(entity: Service): Boolean = with(entity) {
        return id.isNotEmpty() && output != null && name.isNullOrBlank().not()
    }
}
