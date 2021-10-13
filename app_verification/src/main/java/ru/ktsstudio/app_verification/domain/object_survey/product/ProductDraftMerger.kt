package ru.ktsstudio.app_verification.domain.object_survey.product

import ru.ktsstudio.app_verification.domain.object_survey.common.VerificationObjectDraftMerger
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectWithCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.productionCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.productionSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.wasteDisposalCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.wasteDisposalSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.wasteRecyclingCheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.wasteRecyclingSurvey

class ProductDraftMerger : VerificationObjectDraftMerger<ProductionSurveyDraft> {
    override fun invoke(
        verificationObjectWithSurvey: VerificationObjectWithCheckedSurvey,
        draft: ProductionSurveyDraft
    ): VerificationObjectWithCheckedSurvey {
        val survey = verificationObjectWithSurvey.verificationObject.survey
        val checkedSurvey = verificationObjectWithSurvey.checkedSurvey

        return verificationObjectWithSurvey.copy(
            verificationObject = verificationObjectWithSurvey.verificationObject
                .copy(survey = updateSurvey(survey, draft.productionSurvey)),
            checkedSurvey = updateCheckedSurvey(checkedSurvey, draft.productionCheckedSurvey)
        )
    }

    private fun updateCheckedSurvey(
        checkedSurvey: CheckedSurvey,
        update: ProductionCheckedSurvey
    ): CheckedSurvey {
        return when (checkedSurvey) {
            is CheckedSurvey.WasteDisposalCheckedSurvey -> {
                CheckedSurvey.wasteDisposalCheckedSurvey
                    .productionCheckedSurvey
                    .set(checkedSurvey, update)
            }
            is CheckedSurvey.WasteRecyclingCheckedSurvey -> {
                CheckedSurvey.wasteRecyclingCheckedSurvey
                    .productionCheckedSurvey
                    .set(checkedSurvey, update)
            }
            else -> {
                error("ProductionSurvey is available only in WasteDisposalSurvey, WasteRecyclingSurvey")
            }
        }
    }

    private fun updateSurvey(
        survey: Survey,
        update: ProductionSurvey
    ): Survey {
        return when (survey) {
            is Survey.WasteDisposalSurvey -> {
                Survey.wasteDisposalSurvey
                    .productionSurvey
                    .set(survey, update)
            }
            is Survey.WasteRecyclingSurvey -> {
                Survey.wasteRecyclingSurvey
                    .productionSurvey
                    .set(survey, update)
            }
            else -> {
                error("ProductionSurvey is available only in WasteDisposalSurvey, WasteRecyclingSurvey")
            }
        }
    }
}
