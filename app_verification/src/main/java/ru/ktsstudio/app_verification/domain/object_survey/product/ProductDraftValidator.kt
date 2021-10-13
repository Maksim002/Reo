package ru.ktsstudio.app_verification.domain.object_survey.product

import ru.ktsstudio.app_verification.domain.object_survey.common.SurveyDraftValidator
import ru.ktsstudio.app_verification.domain.object_survey.common.fill_validator.FillValidator
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.production.Product
import ru.ktsstudio.core_data_verfication_api.data.model.production.Service

class ProductDraftValidator(
    private val productFillValidator: FillValidator<Product>,
    private val serviceFillValidator: FillValidator<Service>
) : SurveyDraftValidator<ProductionSurveyDraft> {
    override fun invoke(draft: ProductionSurveyDraft): Boolean {
        val survey = draft.productionSurvey
        val checkedSurvey = draft.productionCheckedSurvey
        val isProductCapacityValid = checkedSurvey.productCapacity.not() || survey.productCapacity != null
        val isProductsValid = (checkedSurvey.products.not() || survey.products.isNotEmpty()) &&
            survey.products.values.all(productFillValidator::isFilled)
        val isServicesValid = (checkedSurvey.services.not() || survey.services.isNotEmpty()) &&
            survey.services.values.all(serviceFillValidator::isFilled)
        return isProductCapacityValid && isProductsValid && isServicesValid
    }
}
