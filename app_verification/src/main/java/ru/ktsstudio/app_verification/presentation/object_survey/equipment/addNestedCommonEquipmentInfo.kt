package ru.ktsstudio.app_verification.presentation.object_survey.equipment

import arrow.optics.Optional
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.object_survey.common.StringValueConsumer
import ru.ktsstudio.app_verification.domain.object_survey.common.data_type.EquipmentPhotoUpdater
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.InnerLabeledEditItem
import ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates.media.LabeledMediaListItem
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.text_format.TextFormat
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.brand
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.commonMediaInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.manufacturer
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.nullableCount
import ru.ktsstudio.core_data_verfication_api.data.model.nullablePassport
import ru.ktsstudio.core_data_verfication_api.data.model.photos

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.12.2020.
 */

fun <DraftType> MutableList<Any>.addNestedCommonEquipmentInfo(
    equipmentId: String,
    info: CommonEquipmentInfo,
    updaterOptics: Optional<DraftType, CommonEquipmentInfo>,
    resourceManager: ResourceManager,
    withEquipmentCount: Boolean = true,
    withEquipmentPassport: Boolean = true
) {
    listOfNotNull(
        InnerLabeledEditItem(
            entityId = equipmentId,
            inCard = true,
            label = resourceManager.getString(R.string.survey_equipment_label_count),
            editHint = resourceManager.getString(R.string.survey_equipment_count_hint),
            inputFormat = TextFormat.Number(),
            valueConsumer = StringValueConsumer<DraftType>(
                value = info.count?.toString().orEmpty()
            ) { value, updatable ->
                updaterOptics
                    .nullableCount
                    .set(updatable, value?.toIntOrNull())
            }
        ).takeIf { withEquipmentCount },
        InnerLabeledEditItem(
            entityId = equipmentId,
            inCard = true,
            label = resourceManager.getString(R.string.survey_equipment_label_brand),
            editHint = resourceManager.getString(R.string.survey_equipment_hint_brand),
            inputFormat = TextFormat.Text,
            valueConsumer = StringValueConsumer<DraftType>(
                value = info.brand
            ) { value, updatable ->
                updaterOptics
                    .brand
                    .set(updatable, value.orEmpty())
            }
        ),
        InnerLabeledEditItem(
            entityId = equipmentId,
            inCard = true,
            label = resourceManager.getString(R.string.survey_equipment_label_manufacturer),
            editHint = resourceManager.getString(R.string.survey_equipment_hint_manufacturer),
            inputFormat = TextFormat.Text,
            valueConsumer = StringValueConsumer<DraftType>(
                value = info.manufacturer
            ) { value, updatable ->
                updaterOptics
                    .manufacturer
                    .set(updatable, value.orEmpty())
            }
        ),
        LabeledMediaListItem(
            identifier = CommonEquipmentsFields.Photos(equipmentId),
            inCard = true,
            isNested = true,
            label = resourceManager.getString(R.string.survey_equipment_label_photo),
            valueConsumer = EquipmentPhotoUpdater<DraftType>(
                photos = info.commonMediaInfo.photos,
                setEquipment = { draft, newPhotos ->
                    updaterOptics
                        .commonMediaInfo
                        .photos
                        .set(draft, newPhotos)
                }
            ),
        ),
        LabeledMediaListItem(
            identifier = CommonEquipmentsFields.Passport(equipmentId),
            label = resourceManager.getString(R.string.survey_equipment_label_passport),
            inCard = true,
            isNested = true,
            valueConsumer = EquipmentPhotoUpdater<DraftType>(
                photos = listOfNotNull(info.commonMediaInfo.passport),
                setEquipment = { draft, newPhotos ->
                    updaterOptics
                        .commonMediaInfo
                        .nullablePassport
                        .set(draft, newPhotos.lastOrNull())
                }
            ),
        ).takeIf { withEquipmentPassport },
    ).let { addAll(it) }
}
