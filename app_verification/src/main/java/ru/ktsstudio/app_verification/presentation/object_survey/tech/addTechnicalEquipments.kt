package ru.ktsstudio.app_verification.presentation.object_survey.tech

import arrow.core.MapK
import arrow.optics.Optional
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.EquipmentPhotoUpdater
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.ReferenceUi
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.TechnicalCardType
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledComment
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItem
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.floatValue
import ru.ktsstudio.common.utils.stringValue
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.brand
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonMediaInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.manufacturer
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableCount
import ru.ktsstudio.core_data_verfication_api.data.model.nullablePassport
import ru.ktsstudio.core_data_verfication_api.data.model.photos
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.technical.commonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.technical.description
import ru.ktsstudio.core_data_verfication_api.data.model.technical.nullableCount
import ru.ktsstudio.core_data_verfication_api.data.model.technical.nullablePower
import ru.ktsstudio.core_data_verfication_api.data.model.technical.nullableType
import ru.ktsstudio.core_data_verfication_api.data.model.technical.production
import ru.ktsstudio.core_data_verfication_api.data.model.technical.type

/**
 * @author Maxim Myalkin (MaxMyalkin) on 25.01.2021.
 */

@OptIn(ExperimentalStdlibApi::class)
fun <DraftType> addTechnicalEquipment(
    entity: TechnicalCardType,
    equipmentList: Collection<TechnicalEquipment>,
    updaterOptics: Optional<DraftType, MapK<String, TechnicalEquipment>>,
    resources: ResourceManager,
    types: List<Reference>?
): List<Any> {
    val items = if (equipmentList.isEmpty()) {
        listOf(
            InnerMediumTitle(
                text = resources.getString(R.string.survey_equipment_empty),
                withAccent = false
            )
        )
    } else {
        equipmentList.flatMapIndexed { index, equipment ->
            val selectedType = types?.find { it.id == equipment.type?.id }
            val isLast = index == equipmentList.size - 1
            listOfNotNull(
                CardCornersItem(isTop = true, isNested = true),
                InnerLabeledEditItem(
                    entityId = equipment.id,
                    inCard = true,
                    label = resources.getString(R.string.survey_equipment_label_count),
                    editHint = resources.getString(R.string.survey_equipment_count_hint),
                    inputFormat = TextFormat.Number(),
                    valueConsumer = StringValueConsumer<DraftType>(
                        value = equipment.count?.toString().orEmpty()
                    ) { value, updatable ->
                        updaterOptics
                            .at(MapK.at(), equipment.id)
                            .some
                            .nullableCount
                            .set(updatable, value?.toIntOrNull())
                    }
                ),
                InnerLabeledEditItem(
                    entityId = equipment.id,
                    inCard = true,
                    label = resources.getString(R.string.survey_equipment_label_brand),
                    editHint = resources.getString(R.string.survey_equipment_hint_brand),
                    inputFormat = TextFormat.Text,
                    valueConsumer = StringValueConsumer<DraftType>(
                        value = equipment.commonEquipmentInfo.brand
                    ) { value, updatable ->
                        updaterOptics
                            .at(MapK.at(), equipment.id)
                            .some
                            .commonEquipmentInfo
                            .brand
                            .set(updatable, value.orEmpty())
                    }
                ),
                InnerLabeledEditItem(
                    entityId = equipment.id,
                    inCard = true,
                    label = resources.getString(R.string.survey_equipment_label_manufacturer),
                    editHint = resources.getString(R.string.survey_equipment_hint_manufacturer),
                    inputFormat = TextFormat.Text,
                    valueConsumer = StringValueConsumer<DraftType>(
                        value = equipment.commonEquipmentInfo.manufacturer
                    ) { value, updatable ->
                        updaterOptics
                            .at(MapK.at(), equipment.id)
                            .some
                            .commonEquipmentInfo
                            .manufacturer
                            .set(updatable, value.orEmpty())
                    }
                ),
                InnerLabeledEditItem(
                    entityId = equipment.id,
                    inCard = true,
                    label = resources.getString(R.string.survey_technical_equipment_power_label),
                    editHint = resources.getString(R.string.survey_power_hint),
                    inputFormat = TextFormat.NumberDecimal(),
                    valueConsumer = StringValueConsumer<DraftType>(
                        value = equipment.power.stringValue()
                    ) { value, updatable ->
                        updaterOptics
                            .at(MapK.at(), equipment.id)
                            .some
                            .nullablePower
                            .set(updatable, value?.floatValue())
                    }
                ),
                InnerLabeledSelector(
                    label = resources.getString(R.string.survey_equipment_label_additional_type),
                    hint = resources.getString(R.string.survey_equipment_hint_additional_type),
                    selectedTitle = selectedType?.name,
                    inCard = true,
                    items = types.orEmpty().map {
                        ReferenceUi(
                            id = it.id,
                            title = it.name,
                            isSelected = it.id == selectedType?.id
                        )
                    },
                    valueConsumer = ReferenceUpdater<DraftType>(
                        reference = selectedType?.id,
                        getReference = { selectedTypeId ->
                            types?.find { it.id == selectedTypeId }
                        },
                        setReference = { draft, update ->
                            updaterOptics
                                .at(MapK.at(), equipment.id)
                                .some
                                .nullableType
                                .set(draft, update)
                        }
                    ),
                    identifier = "equipment${equipment.id} type"
                ),
                InnerLabeledComment(
                    entityId = equipment.id,
                    inCard = true,
                    label = resources.getString(R.string.survey_technical_tech_process_comment),
                    editHint = resources.getString(R.string.survey_tech_process_hint),
                    inputFormat = TextFormat.Text,
                    valueConsumer = StringValueConsumer<DraftType>(
                        value = equipment.description
                    ) { value, updatable ->
                        updaterOptics
                            .at(MapK.at(), equipment.id)
                            .some
                            .description
                            .set(updatable, value.orEmpty())
                    }
                ),
                InnerLabeledComment(
                    entityId = equipment.id,
                    inCard = true,
                    label = resources.getString(R.string.survey_technical_equipment_production_label),
                    editHint = resources.getString(R.string.survey_technical_equipment_production_hint),
                    inputFormat = TextFormat.Text,
                    valueConsumer = StringValueConsumer<DraftType>(
                        value = equipment.production
                    ) { value, updatable ->
                        updaterOptics
                            .at(MapK.at(), equipment.id)
                            .some
                            .production
                            .set(updatable, value.orEmpty())
                    }
                ),
                LabeledMediaListItem(
                    identifier = equipment.id,
                    inCard = true,
                    isNested = true,
                    label = resources.getString(R.string.survey_technical_equipment_photo),
                    valueConsumer = EquipmentPhotoUpdater<DraftType>(
                        photos = equipment.commonEquipmentInfo.commonMediaInfo.photos,
                        setEquipment = { draft, newPhotos ->
                            updaterOptics
                                .at(MapK.at(), equipment.id)
                                .some
                                .commonEquipmentInfo
                                .commonMediaInfo
                                .photos
                                .set(draft, newPhotos)
                        }
                    ),
                ),
                LabeledMediaListItem(
                    identifier = equipment.id,
                    label = resources.getString(R.string.survey_equipment_label_passport),
                    inCard = true,
                    isNested = true,
                    valueConsumer = EquipmentPhotoUpdater<DraftType>(
                        photos = listOfNotNull(equipment.commonEquipmentInfo.commonMediaInfo.passport),
                        setEquipment = { draft, newPhotos ->
                            updaterOptics
                                .at(MapK.at(), equipment.id)
                                .some
                                .commonEquipmentInfo
                                .commonMediaInfo
                                .nullablePassport
                                .set(draft, newPhotos.lastOrNull())
                        }
                    ),
                ),
                DeleteEntityItem(
                    inCard = true,
                    id = equipment.id,
                    qualifier = entity
                ),
                CardCornersItem(isTop = false, isNested = true),
                EmptySpace(isNested = true).takeUnless { isLast }
            )
        }
    }

    val addButton = AddEntityItem(
        nested = true,
        text = resources.getString(R.string.survey_equipment_add),
        qualifier = entity
    )

    return items + addButton
}
