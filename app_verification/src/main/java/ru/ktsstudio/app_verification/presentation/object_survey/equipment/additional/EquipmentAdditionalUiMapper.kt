package ru.ktsstudio.app_verification.presentation.object_survey.equipment.additional

import arrow.core.MapK
import arrow.optics.dsl.at
import arrow.optics.dsl.some
import arrow.optics.extensions.mapk.at.at
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.EquipmentPhotoUpdater
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentCheckableDataType
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.EquipmentSurveyDraft
import ru.ktsstudio.app_verification.domain.object_survey.equipment.models.equipment
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.ReferenceUpdater
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.CommonEquipmentsFields
import ru.ktsstudio.app_verification.presentation.object_survey.equipment.EquipmentEntity
import ru.ktsstudio.app_verification.presentation.object_survey.infrastructure.card_mappers.ReferenceUi
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.DeleteEntityItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.EmptySpace
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledSelector
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerMediumTitle
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.SubtitleItemWithCheck
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItem
import ru.ktsstudio.common.ui.adapter.delegates.AddEntityItem
import ru.ktsstudio.common.ui.adapter.delegates.CardCornersItem
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLine
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.additionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.brand
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonMediaInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.manufacturer
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableCount
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableOtherName
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableType
import ru.ktsstudio.core_data_verfication_api.data.model.nullablePassport
import ru.ktsstudio.core_data_verfication_api.data.model.photos
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.utilities.extensions.takeIfNotEmpty

class EquipmentAdditionalUiMapper(private val resources: ResourceManager) {
    @OptIn(ExperimentalStdlibApi::class)
    fun map(item: EquipmentSurveyDraft, references: List<Reference>): List<Any> {
        return buildList {
            add(
                SubtitleItemWithCheck(
                    checkableValueConsumer = EquipmentCheckableDataType.Additional(
                        item.checkedEquipment.additionalEquipment
                    ),
                    title = resources.getString(R.string.survey_equipment_additional_equipment_title)
                )
            )
            item.equipment
                .additionalEquipment
                .values
                .toList()
                .takeIfNotEmpty()
                ?.let { addEquipment(it, references) }
                ?: add(getEmpty())
            add(
                AddEntityItem(
                    nested = true,
                    text = resources.getString(R.string.survey_equipment_add),
                    qualifier = EquipmentEntity.Additional
                )
            )
        }
    }

    private fun getEmpty() = InnerMediumTitle(
        text = resources.getString(R.string.survey_equipment_empty),
        withAccent = false
    )

    private fun MutableList<Any>.addEquipment(
        additionalEquipment: List<AdditionalEquipment>,
        types: List<Reference>
    ) {
        additionalEquipment.forEachIndexed { index, equipment ->
            val isLast = index == additionalEquipment.lastIndex
            val type = types.find { it.id == equipment.type?.id }
            val commonEquipmentInfo = equipment.commonEquipmentInfo
            val commonEquipmentOptics = EquipmentSurveyDraft.equipment
                .additionalEquipment
                .at(MapK.at(), equipment.id)
                .some
                .commonEquipmentInfo
            add(CardCornersItem(isTop = true, isNested = true))
            add(
                InnerLabeledEditItem(
                    entityId = equipment.id,
                    inCard = true,
                    label = resources.getString(R.string.survey_equipment_label_count),
                    editHint = resources.getString(R.string.survey_equipment_count_hint),
                    inputFormat = TextFormat.Number(),
                    valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                        value = commonEquipmentInfo.count?.toString().orEmpty()
                    ) { value, updatable ->
                        commonEquipmentOptics
                            .nullableCount
                            .set(updatable, value?.toIntOrNull())
                    }
                )
            )
            add(
                InnerLabeledSelector(
                    label = resources.getString(R.string.survey_equipment_label_additional_type),
                    hint = resources.getString(R.string.survey_equipment_hint_additional_type),
                    selectedTitle = type?.name,
                    items = types.map {
                        ReferenceUi(
                            id = it.id,
                            title = it.name,
                            isSelected = it.id == equipment.type?.id
                        )
                    },
                    valueConsumer = ReferenceUpdater<EquipmentSurveyDraft>(
                        reference = equipment.type?.id,
                        getReference = { selectedTypeId ->
                            types.find { it.id == selectedTypeId }
                        },
                        setReference = { draft, update ->
                            EquipmentSurveyDraft.equipment
                                .additionalEquipment
                                .at(MapK.at(), equipment.id)
                                .some
                                .nullableType
                                .set(draft, update)
                        }
                    ),
                    inCard = true,
                    identifier = equipment.id
                )
            )
            if (type?.type == ReferenceType.OTHER) {
                add(
                    InnerLabeledEditItem(
                        entityId = equipment.id,
                        inputFormat = TextFormat.Text,
                        label = resources.getString(R.string.survey_equipment_label_additional_other_name),
                        editHint = resources.getString(R.string.survey_name_hint),
                        inCard = true,
                        valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                            value = equipment.otherName
                        ) { value, updatable ->
                            EquipmentSurveyDraft.equipment
                                .additionalEquipment
                                .at(MapK.at(), equipment.id)
                                .some
                                .nullableOtherName
                                .set(updatable, value)
                        }
                    )
                )
            }
            add(
                InnerLabeledEditItem(
                    entityId = equipment.id,
                    inCard = true,
                    label = resources.getString(R.string.survey_equipment_label_brand),
                    editHint = resources.getString(R.string.survey_equipment_hint_brand),
                    inputFormat = TextFormat.Text,
                    valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                        value = commonEquipmentInfo.brand
                    ) { value, updatable ->
                        commonEquipmentOptics
                            .brand
                            .set(updatable, value.orEmpty())
                    }
                )
            )
            add(
                InnerLabeledEditItem(
                    entityId = equipment.id,
                    inCard = true,
                    label = resources.getString(R.string.survey_equipment_label_manufacturer),
                    editHint = resources.getString(R.string.survey_equipment_hint_manufacturer),
                    inputFormat = TextFormat.Text,
                    valueConsumer = StringValueConsumer<EquipmentSurveyDraft>(
                        value = commonEquipmentInfo.manufacturer
                    ) { value, updatable ->
                        commonEquipmentOptics
                            .manufacturer
                            .set(updatable, value.orEmpty())
                    }
                )
            )
            add(
                LabeledMediaListItem(
                    identifier = CommonEquipmentsFields.Photos(equipment.id),
                    inCard = true,
                    isNested = true,
                    label = resources.getString(R.string.survey_equipment_label_photo),
                    valueConsumer = EquipmentPhotoUpdater<EquipmentSurveyDraft>(
                        photos = commonEquipmentInfo.commonMediaInfo.photos,
                        setEquipment = { draft, newPhotos ->
                            commonEquipmentOptics
                                .commonMediaInfo
                                .photos
                                .set(draft, newPhotos)
                        }
                    )
                )
            )
            add(
                LabeledMediaListItem(
                    identifier = CommonEquipmentsFields.Passport(equipment.id),
                    label = resources.getString(R.string.survey_equipment_label_passport),
                    inCard = true,
                    isNested = true,
                    valueConsumer = EquipmentPhotoUpdater<EquipmentSurveyDraft>(
                        photos = listOfNotNull(commonEquipmentInfo.commonMediaInfo.passport),
                        setEquipment = { draft, newPhotos ->
                            commonEquipmentOptics
                                .commonMediaInfo
                                .nullablePassport
                                .set(draft, newPhotos.lastOrNull())
                        }
                    )
                )
            )
            add(
                CardEmptyLine(
                    height = resources.getDimensionPixelSize(R.dimen.default_padding),
                    horizontalPadding = resources.getDimensionPixelSize(R.dimen.default_side_padding),
                    isNested = true
                )
            )
            add(
                DeleteEntityItem(
                    id = equipment.id,
                    inCard = true,
                    qualifier = EquipmentEntity.Additional
                )
            )
            add(CardCornersItem(isTop = false, isNested = true))
            EmptySpace(isNested = true).takeUnless { isLast }?.also(::add)
        }
    }
}
