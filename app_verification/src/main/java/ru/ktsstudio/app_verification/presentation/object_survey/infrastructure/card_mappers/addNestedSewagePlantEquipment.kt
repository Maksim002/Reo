package ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.EquipmentPhotoUpdater
import ru.ktsstudio.app_verification.domain.object_survey.infrastructure.models.InfrastructureSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlantEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullablePassport
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.nullablePower
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.photos
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.type
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */

fun MutableList<Any>.addNestedSewagePlantEquipment(
    equipment: SewagePlantEquipment,
    updaterOptics: Optional<InfrastructureSurveyDraft, SewagePlantEquipment>,
    types: List<Reference>,
    resources: ResourceManager
) {
    val selectedType = types.find { it.id == equipment.type?.id }
    listOf(
        InnerLabeledSelector(
            label = resources.getString(R.string.survey_infrastructure_sewage_plant_type_label),
            hint = resources.getString(R.string.survey_infrastructure_survey_select_type_hint),
            selectedTitle = selectedType?.name,
            items = types.map {
                ReferenceUi(
                    id = it.id,
                    title = it.name,
                    isSelected = it.id == selectedType?.id
                )
            },
            valueConsumer = ReferenceUpdater<InfrastructureSurveyDraft>(
                reference = selectedType?.id,
                getReference = { selectedTypeId ->
                    types.find { it.id == selectedTypeId }
                },
                setReference = { draft, update ->
                    updaterOptics.type
                        .set(draft, update)
                }
            ),
            inCard = true,
            identifier = equipment.id
        ),
        InnerLabeledEditItem(
            label = resources.getString(R.string.survey_infrastructure_sewage_plant_power_label),
            editHint = resources.getString(R.string.survey_power_hint),
            inputFormat = TextFormat.Number(),
            valueConsumer = StringValueConsumer<InfrastructureSurveyDraft>(
                value = equipment.power?.toString(),
                updater = { update, draft ->
                    updaterOptics.nullablePower
                        .set(draft, update?.floatValue())
                }
            ),
            inCard = true,
            entityId = SewagePlantFields.Power(equipment.id)
        ),
        LabeledMediaListItem(
            label = resources.getString(R.string.survey_equipment_label_photo),
            valueConsumer = EquipmentPhotoUpdater<InfrastructureSurveyDraft>(
                photos = equipment.photos,
                setEquipment = { draft, newPhotos ->
                    updaterOptics.photos
                        .set(draft, newPhotos)
                }
            ),
            identifier = SewagePlantFields.Photos(equipment.id),
            isNested = true,
            inCard = true
        ),
        LabeledMediaListItem(
            label = resources.getString(R.string.survey_infrastructure_equipment_passport_label),
            valueConsumer = EquipmentPhotoUpdater<InfrastructureSurveyDraft>(
                photos = listOfNotNull(equipment.passport),
                setEquipment = { draft, newPhotos ->
                    updaterOptics.nullablePassport
                        .set(draft, newPhotos.lastOrNull())
                }
            ),
            identifier = SewagePlantFields.Passport(equipment.id),
            isNested = true,
            inCard = true
        )
    ).let { addAll(it) }
}

private sealed class SewagePlantFields {
    data class Photos(val id: String) : SewagePlantFields()
    data class Passport(val id: String) : SewagePlantFields()
    data class Count(val id: String) : SewagePlantFields()
    data class Power(val id: String) : SewagePlantFields()
}
