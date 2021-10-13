package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.CommonMediaInfo
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControlEquipment
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 21.12.2020.
 */
class WeightControlEquipmentMapper @Inject constructor(
    private val mediaMapper: Mapper<SerializableMedia, Media>
) : Mapper<RemoteInfrastructureObject, WeightControlEquipment> {
    override fun map(item: RemoteInfrastructureObject): WeightControlEquipment = with(item) {
        WeightControlEquipment(
            id = id.requireNotNull(),
            remoteId = id,
            weightPlatformLength = length,
            commonEquipmentInfo = CommonEquipmentInfo(
                brand = brand.orEmpty(),
                manufacturer = manufacturer.orEmpty(),
                count = count,
                commonMediaInfo = CommonMediaInfo(
                    photos = mediaMapper.map(photos.orEmpty()),
                    passport = passport?.firstOrNull()?.let(mediaMapper::map)
                )
            )
        )
    }
}