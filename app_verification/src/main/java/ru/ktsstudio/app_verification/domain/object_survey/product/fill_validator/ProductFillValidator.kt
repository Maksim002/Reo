package ru.ktsstudio.app_verification.domain.object_survey.product.fill_validator

import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.core_data_verfication_api.data.model.production.Product
import javax.inject.Inject

class ProductFillValidator @Inject constructor() : FillValidator<Product> {
    override fun isFilled(entity: Product): Boolean = with(entity) {
        return id.isNotEmpty() && photos.isNotEmpty() && output != null && name.isNullOrBlank().not()
    }
}
