package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.BagBreakerConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Conveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Press
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Separator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ServingConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SortConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.getConveyorType
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.getPressType
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.getSeparatorType
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.isAdditionalKind
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.isConveyorKind
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.isPressKind
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.isSeparatorKind
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteEquipment
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 21.12.2020.
 */
class EquipmentSurveySendMapper @Inject constructor(
    private val mediaMapper: Mapper2<Media, Map<String, LocalMedia>, SerializableMedia>,
    private val referenceMapper: Mapper<Reference, RemoteReference>
) {
    fun map(
        item1: Survey.WasteTreatmentSurvey.EquipmentSurvey,
        item2: List<Reference>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteEquipment> = with(item1) {
        val remoteReferences = referenceMapper.map(item2)
        val conveyorReferences = remoteReferences.filter { it.type.name == ReferenceType.CONVEYOR }
        val conveyorKind = remoteReferences.find { it.isConveyorKind() }
        val separatorReferences = remoteReferences.filter { it.type.name == ReferenceType.SEPARATOR }
        val pressReferences = remoteReferences.filter { it.type.name == ReferenceType.PRESS }
        val additionalKind = remoteReferences.find { it.isAdditionalKind() }
        val separatorKind = remoteReferences.find { it.isSeparatorKind() }
        val pressKind = remoteReferences.find { it.isPressKind() }

        val equipment = mapServingConveyor(
            servingConveyors.values.toList(),
            conveyorReferences,
            localPathToMediaMap,
            conveyorKind
        ) +
            mapSortConveyors(
                sortConveyors.values.toList(),
                conveyorReferences,
                localPathToMediaMap,
                conveyorKind
            ) +
            mapReverseConveyors(
                reverseConveyors.values.toList(),
                conveyorReferences,
                localPathToMediaMap,
                conveyorKind
            ) +
            mapPressConveyors(
                pressConveyors.values.toList(),
                conveyorReferences,
                localPathToMediaMap
            ) +
            mapOtherConveyors(
                otherConveyors.values.toList(),
                localPathToMediaMap,
                conveyorKind
            ) +
            mapBagBreakers(
                bagBreakers.values.toList(),
                conveyorReferences,
                localPathToMediaMap,
                conveyorKind
            ) +
            mapSeparators(
                separators.values.toList(),
                separatorKind,
                separatorReferences,
                localPathToMediaMap
            ) +
            mapPresses(
                presses.values.toList(),
                pressKind,
                pressReferences,
                localPathToMediaMap
            ) +
            mapAdditionalEquipment(
                additionalEquipment.values.toList(),
                additionalKind,
                localPathToMediaMap
            )

        return equipment.sortedBy { it.created }
    }

    private fun mapServingConveyor(
        items: List<ServingConveyor>,
        references: List<RemoteReference>,
        localPathToMediaMap: Map<String, LocalMedia>,
        conveyorKind: RemoteReference?
    ): List<RemoteEquipment> {
        val type = references.find { it.getConveyorType() == ConveyorType.SERVING }
        return items.map {
            RemoteEquipment(
                id = it.id,
                brand = it.commonEquipmentInfo.brand,
                manufacturer = it.commonEquipmentInfo.brand,
                count = it.commonEquipmentInfo.count,
                photos = it.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, localPathToMediaMap) },
                passport = it.commonEquipmentInfo
                    .commonMediaInfo
                    .passport
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, localPathToMediaMap) },
                length = it.commonConveyorInfo.length,
                width = it.commonConveyorInfo.width,
                speed = it.commonConveyorInfo.speed,
                name = null,
                loadingMechanism = it.loadMechanism,
                type = type,
                kind = conveyorKind,
                wasteHeight = null,
                sortPointCount = null,
                otherEquipmentName = null,
                created = it.created?.toEpochMilli(),
                power = null,
                description = null,
                production = null
            )
        }
    }

    private fun mapSortConveyors(
        items: List<SortConveyor>,
        references: List<RemoteReference>,
        mediaLocalPathToRemoteIdMap: Map<String, LocalMedia>,
        conveyorKind: RemoteReference?
    ): List<RemoteEquipment> {
        val type = references.find { it.getConveyorType() == ConveyorType.SORT }
        return items.map {
            RemoteEquipment(
                id = it.id,
                brand = it.commonEquipmentInfo.brand,
                manufacturer = it.commonEquipmentInfo.brand,
                count = it.commonEquipmentInfo.count,
                photos = it.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, mediaLocalPathToRemoteIdMap) },
                passport = it.commonEquipmentInfo
                    .commonMediaInfo
                    .passport
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, mediaLocalPathToRemoteIdMap) },
                length = it.commonConveyorInfo.length,
                width = it.commonConveyorInfo.width,
                speed = it.commonConveyorInfo.speed,
                name = null,
                wasteHeight = it.wasteHeight,
                sortPointCount = it.sortPointCount,
                type = type,
                kind = conveyorKind,
                loadingMechanism = null,
                otherEquipmentName = null,
                created = it.created?.toEpochMilli(),
                power = null,
                description = null,
                production = null
            )
        }
    }

    private fun mapReverseConveyors(
        items: List<Conveyor>,
        references: List<RemoteReference>,
        localPathToMediaMap: Map<String, LocalMedia>,
        conveyorKind: RemoteReference?
    ): List<RemoteEquipment> {
        val type = references.find { it.getConveyorType() == ConveyorType.REVERSE }
        return items.map {
            RemoteEquipment(
                id = it.id,
                brand = it.commonEquipmentInfo.brand,
                manufacturer = it.commonEquipmentInfo.brand,
                count = it.commonEquipmentInfo.count,
                photos = it.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, localPathToMediaMap) },
                passport = it.commonEquipmentInfo
                    .commonMediaInfo
                    .passport
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, localPathToMediaMap) },
                length = it.commonConveyorInfo.length,
                width = it.commonConveyorInfo.width,
                speed = it.commonConveyorInfo.speed,
                name = null,
                type = type,
                kind = conveyorKind,
                loadingMechanism = null,
                wasteHeight = null,
                sortPointCount = null,
                otherEquipmentName = null,
                created = it.created?.toEpochMilli(),
                power = null,
                description = null,
                production = null
            )
        }
    }

    private fun mapPressConveyors(
        items: List<Conveyor>,
        references: List<RemoteReference>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteEquipment> {
        val type = references.find { it.getConveyorType() == ConveyorType.PRESS }
        val conveyorKind = references.find { it.isConveyorKind() }

        return items.map {
            RemoteEquipment(
                id = it.id,
                brand = it.commonEquipmentInfo.brand,
                manufacturer = it.commonEquipmentInfo.brand,
                count = it.commonEquipmentInfo.count,
                photos = it.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, localPathToMediaMap) },
                passport = it.commonEquipmentInfo
                    .commonMediaInfo.passport
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, localPathToMediaMap) },
                length = it.commonConveyorInfo.length,
                width = it.commonConveyorInfo.width,
                speed = it.commonConveyorInfo.speed,
                name = null,
                type = type,
                kind = conveyorKind,
                loadingMechanism = null,
                wasteHeight = null,
                sortPointCount = null,
                otherEquipmentName = null,
                created = it.created?.toEpochMilli(),
                power = null,
                description = null,
                production = null
            )
        }
    }

    private fun mapOtherConveyors(
        items: List<Conveyor>,
        localPathToMediaMap: Map<String, LocalMedia>,
        conveyorKind: RemoteReference?
    ): List<RemoteEquipment> {
        return items.map {
            RemoteEquipment(
                id = it.id,
                brand = it.commonEquipmentInfo.brand,
                manufacturer = it.commonEquipmentInfo.brand,
                count = it.commonEquipmentInfo.count,
                photos = it.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, localPathToMediaMap) },
                passport = it.commonEquipmentInfo
                    .commonMediaInfo
                    .passport
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, localPathToMediaMap) },
                length = it.commonConveyorInfo.length,
                width = it.commonConveyorInfo.width,
                speed = it.commonConveyorInfo.speed,
                name = null,
                type = null,
                kind = conveyorKind,
                loadingMechanism = null,
                wasteHeight = null,
                sortPointCount = null,
                otherEquipmentName = it.otherConveyorName,
                created = it.created?.toEpochMilli(),
                power = null,
                description = null,
                production = null
            )
        }
    }

    private fun mapBagBreakers(
        items: List<BagBreakerConveyor>,
        references: List<RemoteReference>,
        localPathToMediaMap: Map<String, LocalMedia>,
        conveyorKind: RemoteReference?
    ): List<RemoteEquipment> {
        val type = references.find { it.getConveyorType() == ConveyorType.BAG_BREAKER }
        return items.map {
            RemoteEquipment(
                id = it.id,
                brand = it.commonEquipmentInfo.brand,
                manufacturer = it.commonEquipmentInfo.brand,
                count = it.commonEquipmentInfo.count,
                photos = it.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, localPathToMediaMap) },
                passport = it.commonEquipmentInfo
                    .commonMediaInfo
                    .passport
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, localPathToMediaMap) },
                length = null,
                width = null,
                speed = null,
                name = null,
                type = type,
                kind = conveyorKind,
                loadingMechanism = null,
                wasteHeight = null,
                sortPointCount = null,
                otherEquipmentName = null,
                created = it.created?.toEpochMilli(),
                power = null,
                description = null,
                production = null
            )
        }
    }

    private fun mapSeparators(
        items: List<Separator>,
        separatorKind: RemoteReference?,
        references: List<RemoteReference>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteEquipment> {
        return items.map { separator ->
            val type = references.find { reference ->
                reference.getSeparatorType() == separator.type
            }
                .takeIf { separator.type != SeparatorType.OTHER }

            RemoteEquipment(
                id = separator.id,
                brand = separator.commonEquipmentInfo.brand,
                manufacturer = separator.commonEquipmentInfo.brand,
                count = separator.commonEquipmentInfo.count,
                photos = separator.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, localPathToMediaMap) },
                passport = separator.commonEquipmentInfo
                    .commonMediaInfo
                    .passport
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, localPathToMediaMap) },
                length = null,
                width = null,
                speed = null,
                name = null,
                type = type,
                kind = separatorKind,
                loadingMechanism = null,
                wasteHeight = null,
                sortPointCount = null,
                otherEquipmentName = separator.otherName,
                created = separator.created?.toEpochMilli(),
                power = null,
                description = null,
                production = null
            )
        }
    }

    private fun mapPresses(
        items: List<Press>,
        pressKind: RemoteReference?,
        references: List<RemoteReference>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteEquipment> {
        return items.map { press ->
            val type = references.find { reference ->
                reference.getPressType() == press.type
            }
                .takeIf { press.type != PressType.OTHER }
            RemoteEquipment(
                id = press.id,
                brand = press.commonEquipmentInfo.brand,
                manufacturer = press.commonEquipmentInfo.brand,
                count = press.commonEquipmentInfo.count,
                photos = press.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, localPathToMediaMap) },
                passport = press.commonEquipmentInfo
                    .commonMediaInfo
                    .passport
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, localPathToMediaMap) },
                length = null,
                width = null,
                speed = null,
                name = null,
                type = type,
                kind = pressKind,
                loadingMechanism = null,
                wasteHeight = null,
                sortPointCount = null,
                otherEquipmentName = press.otherName,
                created = press.created?.toEpochMilli(),
                power = null,
                description = null,
                production = null
            )
        }
    }

    private fun mapAdditionalEquipment(
        items: List<AdditionalEquipment>,
        additionalKind: RemoteReference?,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteEquipment> {
        return items.map { additionalEquipment ->
            RemoteEquipment(
                id = additionalEquipment.id,
                brand = additionalEquipment.commonEquipmentInfo.brand,
                manufacturer = additionalEquipment.commonEquipmentInfo.brand,
                count = additionalEquipment.commonEquipmentInfo.count,
                photos = additionalEquipment.commonEquipmentInfo
                    .commonMediaInfo
                    .photos
                    .let { mediaMapper.map(it, localPathToMediaMap) },
                passport = additionalEquipment.commonEquipmentInfo
                    .commonMediaInfo
                    .passport
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, localPathToMediaMap) },
                length = null,
                width = null,
                speed = null,
                name = null,
                type = additionalEquipment.type
                    ?.takeIf { it.type != ReferenceType.OTHER }
                    ?.let(referenceMapper::map),
                kind = additionalKind,
                loadingMechanism = null,
                wasteHeight = null,
                sortPointCount = null,
                otherEquipmentName = additionalEquipment.otherName,
                created = additionalEquipment.created?.toEpochMilli(),
                power = null,
                description = null,
                production = null
            )
        }
    }
}