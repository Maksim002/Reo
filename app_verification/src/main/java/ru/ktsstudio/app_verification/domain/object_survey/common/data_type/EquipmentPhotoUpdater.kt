package ru.ktsstudio.app_verification.domain.object_survey.common.data_type

import ru.ktsstudio.app_verification.ui.common.Updater
import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.core_data_verfication_api.data.model.Media

data class EquipmentPhotoUpdater<DraftType>(
    val photos: List<Media>,
    val setEquipment: (DraftType, List<Media>) -> DraftType
) : ValueConsumer<List<Media>, DraftType> {

    override fun get(): List<Media> = photos

    override fun consume(value: List<Media>): Updater<DraftType> {
        return copy(photos = value)
    }

    override fun update(updatable: DraftType): DraftType {
        return setEquipment(
            updatable,
            photos
        )
    }
}
