package ru.ktsstudio.app_verification.presentation.object_survey.equipment.production

import arrow.core.MapK
import arrow.optics.Optional
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.product.models.productionSurvey
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.production.Service
import ru.ktsstudio.core_data_verfication_api.data.model.production.name
import ru.ktsstudio.core_data_verfication_api.data.model.production.nullableOutput
import ru.ktsstudio.core_data_verfication_api.data.model.services

fun MutableList<Any>.addNestedServices(
    item: ProductionSurveyDraft,
    resourceManager: ResourceManager
) {
    val lastIndex = item.productionSurvey.services.size - 1
    item.productionSurvey.services.values.forEachIndexed { index, service ->

        val isLast = index == lastIndex
        add(CardCornersItem(isTop = true, isNested = true))

        addNestedServiceInfo(
            service = service,
            updaterOptics = ProductionSurveyDraft.productionSurvey
                .services
                .at(MapK.at(), service.id)
                .some,
            resourceManager = resourceManager
        )
        add(
            DeleteEntityItem(
                id = service.id,
                qualifier = ProducedEntityType.Service,
                inCard = true
            )
        )
        add(CardCornersItem(isTop = false, isNested = true))
        if (isLast.not()) add(EmptySpace(isNested = true))
    }
}

private fun MutableList<Any>.addNestedServiceInfo(
    service: Service,
    updaterOptics: Optional<ProductionSurveyDraft, Service>,
    resourceManager: ResourceManager
) {
    listOf(
        InnerLabeledEditItem(
            entityId = service.id,
            label = resourceManager.getString(R.string.survey_production_service_name_label),
            editHint = resourceManager.getString(R.string.survey_name_hint),
            inputFormat = TextFormat.Text,
            valueConsumer = StringValueConsumer<ProductionSurveyDraft>(
                value = service.name
            ) { value, updatable ->
                updaterOptics
                    .name
                    .set(updatable, value.orEmpty())
            },
            inCard = true
        ),
        InnerLabeledEditItem(
            entityId = service.id,
            label = resourceManager.getString(R.string.survey_production_volume_services_label),
            editHint = resourceManager.getString(R.string.survey_production_volume_services_hint),
            inputFormat = TextFormat.Number(),
            valueConsumer = StringValueConsumer<ProductionSurveyDraft>(
                value = service.output?.toString()
            ) { value, updatable ->
                updaterOptics
                    .nullableOutput
                    .set(updatable, value?.toIntOrNull())
            },
            inCard = true
        ),
    ).let { addAll(it) }
}
