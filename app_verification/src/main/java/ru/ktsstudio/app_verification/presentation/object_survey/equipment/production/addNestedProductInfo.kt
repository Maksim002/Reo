package ru.ktsstudio.app_verification.presentation.object_survey.equipment.production

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.EquipmentPhotoUpdater
import ru.ktsstudio.app_verification.domain.object_survey.product.models.ProductionSurveyDraft
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.production.Product
import ru.ktsstudio.core_data_verfication_api.data.model.production.name
import ru.ktsstudio.core_data_verfication_api.data.model.production.nullableOutput
import ru.ktsstudio.core_data_verfication_api.data.model.production.photos

fun MutableList<Any>.addNestedProductInfo(
    productId: String,
    product: Product,
    updaterOptics: Optional<ProductionSurveyDraft, Product>,
    resourceManager: ResourceManager
) {
    listOf(
        InnerLabeledEditItem(
            entityId = productId,
            label = resourceManager.getString(R.string.survey_production_name_label),
            editHint = resourceManager.getString(R.string.survey_name_hint),
            inputFormat = TextFormat.Text,
            valueConsumer = StringValueConsumer<ProductionSurveyDraft>(
                value = product.name
            ) { value, updatable ->
                updaterOptics
                    .name
                    .set(updatable, value.orEmpty())
            },
            inCard = true
        ),
        InnerLabeledEditItem(
            entityId = productId,
            label = resourceManager.getString(R.string.survey_production_volume_label),
            editHint = resourceManager.getString(R.string.survey_production_volume_hint),
            inputFormat = TextFormat.Number(),
            valueConsumer = StringValueConsumer<ProductionSurveyDraft>(
                value = product.output?.toString()
            ) { value, updatable ->
                updaterOptics
                    .nullableOutput
                    .set(updatable, value?.toIntOrNull())
            },
            inCard = true
        ),
        LabeledMediaListItem(
            identifier = ProductFields.Photos,
            label = resourceManager.getString(R.string.survey_production_photo_label),
            valueConsumer = EquipmentPhotoUpdater<ProductionSurveyDraft>(
                photos = product.photos,
                setEquipment = { draft, newPhotos ->
                    updaterOptics.photos
                        .set(draft, newPhotos)
                }
            ),
            inCard = true,
            isNested = true
        )
    ).let { addAll(it) }
}

private sealed class ProductFields {
    object Photos : ProductFields()
}
