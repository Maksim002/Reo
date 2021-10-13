package ru.ktsstudio.app_verification.domain.object_survey.product

import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectDeleter
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.product.models.productionSurvey
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.production.ProducedEntityType
import ru.ktsstudio.core_data_verfication_api.data.model.products
import ru.ktsstudio.core_data_verfication_api.data.model.services

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class ProductEntityDeleter : NestedObjectDeleter<ProductionSurveyDraft, ProducedEntityType> {

    override fun invoke(
        draft: ProductionSurveyDraft,
        id: String,
        entityType: ProducedEntityType
    ): ProductionSurveyDraft {
        return when (entityType) {
            is ProducedEntityType.Product -> draft.deleteProduct(id)
            is ProducedEntityType.Service -> draft.deleteService(id)
        }
    }

    private fun ProductionSurveyDraft.deleteProduct(id: String): ProductionSurveyDraft {
        return ProductionSurveyDraft.productionSurvey
            .products
            .modify(this) {
                it.minus(id).k()
            }
    }

    private fun ProductionSurveyDraft.deleteService(id: String): ProductionSurveyDraft {
        return ProductionSurveyDraft.productionSurvey
            .services
            .modify(this) {
                it.minus(id).k()
            }
    }
}
