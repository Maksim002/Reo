package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.EquipmentKind
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.isEquipmentKind
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteEquipment
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 27.01.2021.
 */
class TechnicalEquipmentMapper @Inject constructor(
    private val mediaMapper: Mapper2<Media, Map<String, LocalMedia>, SerializableMedia>,
    private val referenceMapper: Mapper<Reference, RemoteReference>
) {

    fun map(
        kind: EquipmentKind,
        equipment: List<TechnicalEquipment>,
        references: List<Reference>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteEquipment> {

        val technicalEquipmentKind = references.find {
            referenceMapper.map(it).isEquipmentKind(kind)
        }

        return equipment.map {
            RemoteEquipment(
                id = it.id,
                brand = it.commonEquipmentInfo.brand,
                manufacturer = it.commonEquipmentInfo.manufacturer,
                count = it.count,
                power = it.power,
                description = it.description,
                production = it.production,
                photos = it.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, localPathToMediaMap) },
                passport = it.commonEquipmentInfo
                    .commonMediaInfo
                    .passport
                    ?.let { listOf(it) }
                    ?.let { mediaMapper.map(it, localPathToMediaMap) },
                length = null,
                width = null,
                speed = null,
                name = null,
                type = it.type?.let { referenceMapper.map(it) },
                kind = technicalEquipmentKind?.let { referenceMapper.map(it) },
                loadingMechanism = null,
                wasteHeight = null,
                sortPointCount = null,
                otherEquipmentName = null,
                created = it.created?.toEpochMilli()
            )
        }
    }
}