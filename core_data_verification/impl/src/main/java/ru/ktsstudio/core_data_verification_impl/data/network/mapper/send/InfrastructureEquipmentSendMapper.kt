package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureEquipment
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 21.12.2020.
 */
class InfrastructureEquipmentSendMapper @Inject constructor(
    private val mediaMapper: Mapper2<Media, Map<String, LocalMedia>, SerializableMedia>
) {

    fun map(
        items: List<InfrastructureEquipment>,
        mediaLocalPathToRemoteIdMap: Map<String, LocalMedia>
    ): List<RemoteInfrastructureObject> {
        return items.map { mapSingle(it, mediaLocalPathToRemoteIdMap) }
    }

    private fun mapSingle(
        item: InfrastructureEquipment,
        localPathToMediaMap: Map<String, LocalMedia>
    ): RemoteInfrastructureObject = with(item) {
        return RemoteInfrastructureObject(
            id = remoteId,
            count = commonEquipmentInfo.count,
            brand = commonEquipmentInfo.brand,
            manufacturer = commonEquipmentInfo.manufacturer,
            photos = mediaMapper.map(commonEquipmentInfo.commonMediaInfo.photos, localPathToMediaMap),
            passport = commonEquipmentInfo.commonMediaInfo.passport?.let(::listOf)
                ?.let { mediaMapper.map(it, localPathToMediaMap) }
        )
    }
}