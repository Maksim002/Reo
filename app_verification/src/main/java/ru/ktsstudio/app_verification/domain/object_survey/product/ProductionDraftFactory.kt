package ru.ktsstudio.app_verification.domain.object_survey.product

import ru.ktsstudio.app_verification.domain.object_survey.common.DraftFactory
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey

class ProductionDraftFactory : DraftFactory<ProductionSurveyDraft> {
    override fun invoke(
        verificationObjectWithCheckedSurvey: VerificationObjectWithCheckedSurvey
    ): ProductionSurveyDraft {
        val survey = verificationObjectWithCheckedSurvey.verificationObject.survey
        val checkedSurvey = verificationObjectWithCheckedSurvey.checkedSurvey
        return ProductionSurveyDraft(
            productionSurvey = when (survey) {
                is Survey.WasteDisposalSurvey -> survey.productionSurvey
                is Survey.WasteRecyclingSurvey -> survey.productionSurvey
                else -> {
                    error("ProductionSurvey is not available")
                }
            },
            productionCheckedSurvey = when (checkedSurvey) {
                is CheckedSurvey.WasteDisposalCheckedSurvey -> checkedSurvey.productionCheckedSurvey
                is CheckedSurvey.WasteRecyclingCheckedSurvey -> checkedSurvey.productionCheckedSurvey
                else -> {
                    error("ProductionSurvey is not available")
                }
            }
        )
    }
}
