package ru.ktsstudio.app_verification.presentation.object_survey.equipment.production

import arrow.core.MapK
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.product.models.productionSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.core_data_verfication_api.data.model.products

fun MutableList<Any>.addNestedProducts(
    item: ProductionSurveyDraft,
    resourceManager: ResourceManager
) {
    val lastIndex = item.productionSurvey.products.size - 1
    item.productionSurvey.products.values.forEachIndexed { index, product ->
        val isLast = index == lastIndex
        add(CardCornersItem(isTop = true, isNested = true))

        addNestedProductInfo(
            productId = product.id,
            product = product,
            updaterOptics = ProductionSurveyDraft.productionSurvey
                .products
                .at(MapK.at(), product.id)
                .some,
            resourceManager = resourceManager
        )
        add(
            DeleteEntityItem(
                id = product.id,
                qualifier = ProducedEntityType.Product,
                inCard = true
            )
        )
        add(CardCornersItem(isTop = false, isNested = true))
        if (isLast.not()) add(EmptySpace(isNested = true))
    }
}
