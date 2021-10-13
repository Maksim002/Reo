package ru.ktsstudio.app_verification.domain.object_survey.product

import arrow.core.k
import ru.ktsstudio.app_verification.domain.object_survey.common.nested_object.NestedObjectAdder
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.product.models.productionSurvey
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.production.ProducedEntityType
import ru.ktsstudio.core_data_verfication_api.data.model.production.Product
import ru.ktsstudio.core_data_verfication_api.data.model.production.Service
import ru.ktsstudio.core_data_verfication_api.data.model.products
import ru.ktsstudio.core_data_verfication_api.data.model.services
import java.util.UUID

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
class ProductEntityAdder : NestedObjectAdder<ProductionSurveyDraft, ProducedEntityType> {
    override fun invoke(draft: ProductionSurveyDraft, entityType: ProducedEntityType): ProductionSurveyDraft {
        val id = generateId()
        return when (entityType) {
            is ProducedEntityType.Product -> draft.addNewProduct(id)
            is ProducedEntityType.Service -> draft.addNewService(id)
        }
    }

    private fun generateId(): String = UUID.randomUUID().toString()

    private fun ProductionSurveyDraft.addNewProduct(id: String): ProductionSurveyDraft {
        return ProductionSurveyDraft.productionSurvey.products.modify(this) {
            it.plus(id to Product.createEmpty(id = id)).k()
        }
    }

    private fun ProductionSurveyDraft.addNewService(id: String): ProductionSurveyDraft {
        return ProductionSurveyDraft.productionSurvey.services.modify(this) {
            it.plus(id to Service.createEmpty(id = id)).k()
        }
    }
}
