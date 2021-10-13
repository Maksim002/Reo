package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.CommonMediaInfo
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlantEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 21.12.2020.
 */
class SewagePlantEquipmentMapper @Inject constructor(
    private val mediaMapper: Mapper<SerializableMedia, Media>,
    private val referenceMapper: Mapper<RemoteReference, Reference>,
) : Mapper<RemoteInfrastructureObject, SewagePlantEquipment> {
    override fun map(item: RemoteInfrastructureObject): SewagePlantEquipment = with(item) {
        SewagePlantEquipment(
            id = id.requireNotNull(),
            remoteId = id,
            power = capacity,
            type = type?.let { referenceMapper.map(it) },
            photos = mediaMapper.map(photos.orEmpty()),
            passport = passport?.firstOrNull()?.let(mediaMapper::map)
        )
    }
}