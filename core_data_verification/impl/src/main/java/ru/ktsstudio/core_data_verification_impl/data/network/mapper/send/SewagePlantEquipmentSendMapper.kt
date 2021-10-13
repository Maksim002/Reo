package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlantEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 21.12.2020.
 */
class SewagePlantEquipmentSendMapper @Inject constructor(
    private val mediaMapper: Mapper2<Media, Map<String, LocalMedia>, SerializableMedia>,
    private val referenceMapper: Mapper<Reference, RemoteReference>,
) {

    fun map(
        items: List<SewagePlantEquipment>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteInfrastructureObject> {
        return items.map { mapSingle(it, localPathToMediaMap) }
    }

    private fun mapSingle(
        item: SewagePlantEquipment,
        mediaLocalPathToRemoteIdMap: Map<String, LocalMedia>
    ): RemoteInfrastructureObject = with(item) {
        RemoteInfrastructureObject(
            id = remoteId,
            photos = mediaMapper.map(photos, mediaLocalPathToRemoteIdMap),
            type = type?.let(referenceMapper::map),
            capacity = power,
            passport = passport?.let(::listOf)?.let { mediaMapper.map(it, mediaLocalPathToRemoteIdMap) }
        )
    }
}