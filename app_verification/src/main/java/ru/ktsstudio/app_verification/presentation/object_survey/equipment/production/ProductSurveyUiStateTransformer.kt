package ru.ktsstudio.app_verification.presentation.object_survey.equipment.production

import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.ObjectSurveyFeature
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueCheckableConsumer
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.product.models.productionCheckedSurvey
import ru.ktsstudio.app_verification.domain.object_survey.product.models.productionSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LabeledEditItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.LargeTitleItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.nullableProductCapacity
import ru.ktsstudio.core_data_verfication_api.data.model.productCapacity

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */
internal class ProductSurveyUiStateTransformer(
    private val resourceManager: ResourceManager
) : (ObjectSurveyFeature.State<ProductionSurveyDraft>) -> ProductSurveyUiState {

    override fun invoke(state: ObjectSurveyFeature.State<ProductionSurveyDraft>): ProductSurveyUiState {
        return ProductSurveyUiState(
            loading = state.loading,
            error = state.error,
            data = state.draft?.let(::createItems).orEmpty()
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun createItems(item: ProductionSurveyDraft): List<Any> {
        return buildList {
            add(getTitle())
            add(getProductCapacity(item))
            addProducts(item)
            addServices(item)
        }
    }

    private fun getTitle(): LargeTitleItem {
        return LargeTitleItem(resourceManager.getString(R.string.object_inspection_card_label_product_info))
    }

    private fun getProductCapacity(item: ProductionSurveyDraft): LabeledEditItemWithCheck {
        return LabeledEditItemWithCheck(
            label = resourceManager.getString(R.string.survey_production_production_capacity_label),
            editHint = resourceManager.getString(R.string.survey_production_production_capacity_hint),
            inputFormat = TextFormat.Number(),
            checkableValueConsumer = StringValueCheckableConsumer<ProductionSurveyDraft>(
                isChecked = item.productionCheckedSurvey.productCapacity,
                value = item.productionSurvey.productCapacity?.toString(),
                updater = { capacity, isChecked, draft ->
                    val updatedDraft = ProductionSurveyDraft.productionSurvey
                        .nullableProductCapacity
                        .set(draft, capacity?.toIntOrNull())
                    ProductionSurveyDraft.productionCheckedSurvey
                        .productCapacity
                        .set(updatedDraft, isChecked)
                }
            )

        )
    }

    private fun getEmpty(type: ProducedEntityType): InnerMediumTitle {
        val textRes = when (type) {
            ProducedEntityType.Product -> R.string.survey_production_products_empty_text
            ProducedEntityType.Service -> R.string.survey_production_services_empty_text
        }
        return InnerMediumTitle(
            text = resourceManager.getString(textRes),
            withAccent = false
        )
    }

    private fun getAddButton(type: ProducedEntityType): AddEntityItem<ProducedEntityType> {
        val textRes = when (type) {
            ProducedEntityType.Product -> R.string.survey_production_add_product_type
            ProducedEntityType.Service -> R.string.survey_production_add_service
        }
        return AddEntityItem(
            nested = true,
            text = resourceManager.getString(textRes),
            qualifier = type
        )
    }

    private fun MutableList<Any>.addProducts(item: ProductionSurveyDraft) {
        add(
            SubtitleItemWithCheck(
                checkableValueConsumer = ProductionCheckableDataType.Products(
                    item.productionCheckedSurvey.products
                ),
                title = resourceManager.getString(R.string.survey_production_products_title)
            )
        )
        item.takeIf { it.productionSurvey.products.isNotEmpty() }
            ?.let { addNestedProducts(item, resourceManager) }
            ?: add(getEmpty(ProducedEntityType.Product))
        add(getAddButton(ProducedEntityType.Product))
    }

    private fun MutableList<Any>.addServices(item: ProductionSurveyDraft) {
        add(
            SubtitleItemWithCheck(
                checkableValueConsumer = ProductionCheckableDataType.Services(
                    item.productionCheckedSurvey.services
                ),
                title = resourceManager.getString(R.string.survey_production_services_title)
            )
        )
        item.takeIf { it.productionSurvey.services.isNotEmpty() }
            ?.let { addNestedServices(item, resourceManager) }
            ?: add(getEmpty(ProducedEntityType.Service))
        add(getAddButton(ProducedEntityType.Service))
    }
}
