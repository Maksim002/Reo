package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.CommonMediaInfo
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureEquipment
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 21.12.2020.
 */
class InfrastructureEquipmentMapper @Inject constructor(
    private val mediaMapper: Mapper<SerializableMedia, Media>
) : Mapper<RemoteInfrastructureObject, InfrastructureEquipment> {
    override fun map(item: RemoteInfrastructureObject): InfrastructureEquipment = with(item) {
        InfrastructureEquipment(
            id = id.requireNotNull(),
            remoteId = id,
            commonEquipmentInfo = CommonEquipmentInfo(
                brand = brand.orEmpty(),
                manufacturer = manufacturer.orEmpty(),
                count = count,
                commonMediaInfo = CommonMediaInfo(
                    photos = photos.orEmpty().let(mediaMapper::map),
                    passport = passport?.firstOrNull()?.let(mediaMapper::map)
                )
            )
        )
    }
}