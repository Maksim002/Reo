package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControlEquipment
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 21.12.2020.
 */
class WeightControlEquipmentSendMapper @Inject constructor(
    private val mediaMapper: Mapper2<Media, Map<String, LocalMedia>, SerializableMedia>
) {

    fun map(
        items: List<WeightControlEquipment>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteInfrastructureObject> {
        return items.map {
            mapSingle(it, localPathToMediaMap)
        }
    }

    private fun mapSingle(
        item: WeightControlEquipment,
        localPathToMediaMap: Map<String, LocalMedia>
    ): RemoteInfrastructureObject = with(item) {
        RemoteInfrastructureObject(
            id = remoteId,
            count = commonEquipmentInfo.count,
            brand = commonEquipmentInfo.brand,
            manufacturer = commonEquipmentInfo.manufacturer,
            length = weightPlatformLength,
            photos = commonEquipmentInfo.commonMediaInfo.photos
                .let { mediaMapper.map(it, localPathToMediaMap) },
            passport = commonEquipmentInfo.commonMediaInfo.passport
                ?.let(::listOf)
                ?.let { mediaMapper.map(it, localPathToMediaMap) },
            schemePhotos = emptyList()
        )
    }
}