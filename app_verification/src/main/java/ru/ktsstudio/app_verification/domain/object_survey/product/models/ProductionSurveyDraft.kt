package ru.ktsstudio.app_verification.domain.object_survey.product.models

import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionSurvey

@optics
data class ProductionSurveyDraft(
    val productionSurvey: ProductionSurvey,
    val productionCheckedSurvey: ProductionCheckedSurvey
) {
    companion object
}
