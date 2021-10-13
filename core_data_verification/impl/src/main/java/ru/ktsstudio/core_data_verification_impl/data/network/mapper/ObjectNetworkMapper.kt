package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import arrow.core.k
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.CheckedSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.CommonMediaInfo
import ru.ktsstudio.core_data_verfication_api.data.model.GeneralInformation
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.SurveyStatus
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObject
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.BagBreakerConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonConveyorInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Conveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ConveyorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.EquipmentKind
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Press
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.PressType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Separator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SeparatorType
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ServingConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SortConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourceType
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteEquipment
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteProductsInfo
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableObjectWorkSchedule
import ru.ktsstudio.utilities.extensions.orDefault
import ru.ktsstudio.utilities.extensions.orFalse
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
class ObjectNetworkMapper @Inject constructor(
    private val scheduleSurveyMapper: Mapper<SerializableObjectWorkSchedule, ScheduleSurvey>,
    private val generalInformationMapper: Mapper<RemoteVerificationObject, GeneralInformation>,
    private val referenceRemoteMapper: Mapper<RemoteReference, Reference>,
    private val remoteInfrastructureSurveyMapper: Mapper2<
        RemoteVerificationObject,
        VerificationObjectType,
        InfrastructureSurvey
        >,
    private val productionSurveyMapper: Mapper<RemoteProductsInfo?, ProductionSurvey>,
    private val mediaMapper: Mapper<SerializableMedia, Media>,
    private val wastePlacementMap: Mapper<RemoteWastePlacementMap, WastePlacementMap>,
    private val checkedSurveyMapper: Mapper2<RemoteVerificationObject, VerificationObjectType, CheckedSurvey>
) : Mapper<RemoteVerificationObject, VerificationObject?> {

    override fun map(item: RemoteVerificationObject): VerificationObject? {
        val type = objectTypeToIdMap.filterValues { it == item.type.id }
            .keys
            .firstOrNull()
            ?: return null

        val objectStatus = item.objectStatus?.let(referenceRemoteMapper::map)

        //todo resolve with backend - need status to be null when other status is not
        val hasOtherObjectStatus = item.otherObjectStatusName.isNullOrBlank().not()

        return VerificationObject(
            id = item.id,
            gpsPoint = GpsPoint(
                lat = item.latitude?.toDoubleOrNull().orDefault(0.0),
                lng = item.longitude?.toDoubleOrNull().orDefault(0.0)
            ),
            status = item.technicalSurvey?.status?.let {
                SurveyStatus(
                    name = it.displayName,
                    type = it.statusType
                )
            },
            date = item.technicalSurvey?.date.orDefault(0).let { Instant.ofEpochMilli(it) },
            objectStatus = if (hasOtherObjectStatus) DEFAULT_OTHER_TYPE else objectStatus,
            scheduleSurvey = scheduleSurveyMapper.map(item.workSchedule),
            type = type,
            survey = mapSurvey(type, item),
            generalInformation = generalInformationMapper.map(item),
            otherObjectStatusName = item.otherObjectStatusName,
            checkedSurvey = checkedSurveyMapper.map(item, type)
        )
    }

    private fun mapSurvey(
        type: VerificationObjectType,
        remoteObject: RemoteVerificationObject
    ): Survey {
        val infrastructure = remoteInfrastructureSurveyMapper.map(remoteObject, type)
        val productionSurvey = productionSurveyMapper.map(remoteObject.productionInfo)
        return when (type) {
            VerificationObjectType.WASTE_TREATMENT -> mapWasteTreatmentSurvey(
                remoteObject,
                infrastructure
            )
            VerificationObjectType.WASTE_PLACEMENT -> mapWastePlacementSurvey(
                remoteObject,
                infrastructure
            )
            VerificationObjectType.WASTE_RECYCLING -> mapWasteRecyclingSurvey(
                remoteObject,
                infrastructure,
                productionSurvey
            )
            VerificationObjectType.WASTE_DISPOSAL -> mapWasteDisposalSurvey(
                remoteObject,
                infrastructure,
                productionSurvey
            )
        }
    }

    private fun mapWasteTreatmentSurvey(
        remoteObject: RemoteVerificationObject,
        infrastructureSurvey: InfrastructureSurvey
    ): Survey.WasteTreatmentSurvey {
        return Survey.WasteTreatmentSurvey(
            technicalSurvey = TechnicalSurvey.WasteTreatmentTechnicalSurvey(
                tkoWeightForLastYear = remoteObject.indicator.tkoWeightForLastYear,
                otherWastesWeightForLastYear = remoteObject.indicator.otherWastesWeightForLastYear,
                objectArea = remoteObject.indicator.objectArea,
                objectPhotos = mediaMapper.map(remoteObject.indicator.objectPhotos.orEmpty()),
                productionArea = remoteObject.indicator.productionArea,
                productionPhotos = mediaMapper.map(remoteObject.indicator.productionPhotos.orEmpty()),
                wasteUnloadingArea = remoteObject.indicator.tkoWeightForLastYear,
                sortDepartmentArea = remoteObject.indicator.tkoWeightForLastYear,
                hasCompostArea = remoteObject.indicator.hasCompostArea.orFalse(),
                noCompostAreaReason = remoteObject.indicator.noCompostAreaReason,
                compostAreaPower = remoteObject.indicator.tkoWeightForLastYear,
                compostMaterial = remoteObject.indicator.compostMaterial,
                compostPurpose = remoteObject.indicator.compostPurpose,
                hasRdfArea = remoteObject.indicator.hasRdfArea.orFalse(),
                rdfPower = remoteObject.indicator.tkoWeightForLastYear,
                rdfPurpose = remoteObject.indicator.rdfPurpose,
                techSchema = mediaMapper.map(remoteObject.indicator.techSchema.orEmpty()),
                generalSchema = mediaMapper.map(remoteObject.indicator.generalSchema.orEmpty()),
                productionSchema = mediaMapper.map(remoteObject.indicator.productionSchema.orEmpty())
            ),
            infrastructureSurvey = infrastructureSurvey,
            equipmentSurvey = Survey.WasteTreatmentSurvey.EquipmentSurvey(
                servingConveyors = remoteObject.equipment
                    .getServingConveyors()
                    .associateBy { it.id }.k(),
                sortConveyors = remoteObject.equipment
                    .getSortConveyors()
                    .associateBy { it.id }.k(),
                reverseConveyors = remoteObject.equipment
                    .filter { it.type.getConveyorType() == ConveyorType.REVERSE }
                    .mapCommonConveyors()
                    .associateBy { it.id }.k(),
                pressConveyors = remoteObject.equipment
                    .filter { it.type.getConveyorType() == ConveyorType.PRESS }
                    .mapCommonConveyors()
                    .associateBy { it.id }.k(),
                otherConveyors = remoteObject.equipment
                    .filter { it.isOtherConveyorType() }
                    .mapCommonConveyors()
                    .associateBy { it.id }.k(),
                bagBreakers = remoteObject.equipment
                    .getBagBreakers().associateBy { it.id }.k(),
                separators = remoteObject.equipment.getSeparators().associateBy { it.id }.k(),
                presses = remoteObject.equipment.getPresses().associateBy { it.id }.k(),
                additionalEquipment = remoteObject.equipment
                    .getAdditionalEquipment()
                    .associateBy { it.id }
                    .k()
            ),
            secondaryResourcesSurvey = getSecondaryResources(remoteObject)
        )
    }

    private fun getSecondaryResources(remoteObject: RemoteVerificationObject): SecondaryResourcesSurvey {
        return SecondaryResourcesSurvey(
            extractPercent = remoteObject.secondaryResources?.extractPercent,
            types = remoteObject.secondaryResources?.types.orEmpty().map {
                SecondaryResourceType(
                    id = it.id,
                    reference = it.type?.let(referenceRemoteMapper::map),
                    percent = it.percent,
                    otherName = it.otherName
                )
            }.associateBy { it.id }.k()
        )
    }

    private fun List<RemoteEquipment>.getServingConveyors(): List<ServingConveyor> {
        return filter {
            it.type.getConveyorType() == ConveyorType.SERVING
        }.map {
            ServingConveyor(
                id = it.id,
                commonEquipmentInfo = it.getCommonEquipmentInfo(),
                commonConveyorInfo = CommonConveyorInfo(
                    it.length,
                    it.width,
                    it.speed
                ),
                loadMechanism = it.loadingMechanism.orEmpty(),
                created = it.created?.let(Instant::ofEpochMilli)
            )
        }
    }

    private fun List<RemoteEquipment>.getSortConveyors(): List<SortConveyor> {
        return filter { it.type.getConveyorType() == ConveyorType.SORT }
            .map {
                SortConveyor(
                    id = it.id,
                    commonEquipmentInfo = it.getCommonEquipmentInfo(),
                    commonConveyorInfo = CommonConveyorInfo(
                        it.length,
                        it.width,
                        it.speed,
                    ),
                    wasteHeight = it.wasteHeight,
                    sortPointCount = it.sortPointCount,
                    created = it.created?.let(Instant::ofEpochMilli)
                )
            }
    }

    private fun List<RemoteEquipment>.mapCommonConveyors(): List<Conveyor> {
        return map {
            Conveyor(
                id = it.id,
                commonEquipmentInfo = it.getCommonEquipmentInfo(),
                commonConveyorInfo = CommonConveyorInfo(
                    it.length,
                    it.width,
                    it.speed,
                ),
                otherConveyorName = it.otherEquipmentName,
                created = it.created?.let(Instant::ofEpochMilli)
            )
        }
    }

    private fun List<RemoteEquipment>.getBagBreakers(): List<BagBreakerConveyor> {
        return filter { it.type.getConveyorType() == ConveyorType.BAG_BREAKER }.map {
            BagBreakerConveyor(
                id = it.id,
                commonEquipmentInfo = it.getCommonEquipmentInfo(),
                created = it.created?.let(Instant::ofEpochMilli)
            )
        }
    }

    private fun List<RemoteEquipment>.getSeparators(): List<Separator> {
        return filter { it.type.isSeparator() || it.isOtherSeparator() }.map {
            Separator(
                id = it.id,
                commonEquipmentInfo = it.getCommonEquipmentInfo(),
                type = if (it.isOtherSeparator()) {
                    SeparatorType.OTHER
                } else {
                    it.type.getSeparatorType().requireNotNull()
                },
                otherName = it.otherEquipmentName,
                created = it.created?.let(Instant::ofEpochMilli)
            )
        }
    }

    private fun List<RemoteEquipment>.getPresses(): List<Press> {
        return filter { it.type.isPress() || it.isOtherPress() }
            .map {
                Press(
                    id = it.id,
                    commonEquipmentInfo = it.getCommonEquipmentInfo(),
                    type = if (it.isOtherPress()) {
                        PressType.OTHER
                    } else {
                        it.type.getPressType().requireNotNull()
                    },
                    otherName = it.otherEquipmentName,
                    created = it.created?.let(Instant::ofEpochMilli)
                )
            }
    }

    private fun List<RemoteEquipment>.getAdditionalEquipment(): List<AdditionalEquipment> {
        return filter { it.type.isAdditionalEquipment() || it.isOtherAdditionalEquipment() }
            .map {
                AdditionalEquipment(
                    id = it.id,
                    commonEquipmentInfo = it.getCommonEquipmentInfo(),
                    type = if (it.isOtherAdditionalEquipment()) {
                        DEFAULT_OTHER_TYPE
                    } else {
                        it.type?.let(referenceRemoteMapper::map)
                    },
                    otherName = it.otherEquipmentName,
                    created = it.created?.let(Instant::ofEpochMilli)
                )
            }
    }

    private fun List<RemoteEquipment>.getTechnicalEquipment(kind: EquipmentKind): List<TechnicalEquipment> {
        return filter { it.kind.isEquipmentKind(kind) }
            .map {
                TechnicalEquipment(
                    id = it.id,
                    commonEquipmentInfo = it.getCommonEquipmentInfo(),
                    power = it.power,
                    count = it.count,
                    type = it.type?.let(referenceRemoteMapper::map),
                    description = it.description.orEmpty(),
                    production = it.production.orEmpty(),
                    created = it.created?.let(Instant::ofEpochMilli)
                )
            }
    }

    private fun mapWastePlacementSurvey(
        remoteObject: RemoteVerificationObject,
        infrastructureSurvey: InfrastructureSurvey
    ): Survey.WastePlacementSurvey {
        return Survey.WastePlacementSurvey(
            technicalSurvey = TechnicalSurvey.WastePlacementTechnicalSurvey(
                tkoWeightForLastYear = remoteObject.indicator.tkoWeightForLastYear,
                otherWastesWeightForLastYear = remoteObject.indicator.otherWastesWeightForLastYear,
                objectArea = remoteObject.indicator.objectArea,
                objectPhotos = mediaMapper.map(remoteObject.indicator.objectPhotos.orEmpty()),
                productionArea = remoteObject.indicator.productionArea,
                productionPhotos = mediaMapper.map(remoteObject.indicator.productionPhotos.orEmpty()),
                hasCompostArea = remoteObject.indicator.hasCompostArea.orFalse(),
                noCompostAreaReason = remoteObject.indicator.noCompostAreaReason,
                compostAreaPower = remoteObject.indicator.tkoWeightForLastYear,
                compostMaterial = remoteObject.indicator.compostMaterial,
                compostPurpose = remoteObject.indicator.compostPurpose,
                hasRdfArea = remoteObject.indicator.hasRdfArea.orFalse(),
                rdfPower = remoteObject.indicator.tkoWeightForLastYear,
                rdfPurpose = remoteObject.indicator.rdfPurpose,
                techSchema = mediaMapper.map(remoteObject.indicator.techSchema.orEmpty()),
                generalSchema = mediaMapper.map(remoteObject.indicator.generalSchema.orEmpty()),
                productionSchema = mediaMapper.map(remoteObject.indicator.productionSchema.orEmpty()),
                objectBodyArea = remoteObject.indicator.objectBodyArea,
                polygonHeight = remoteObject.indicator.polygonHeight,
                waterproofingType = remoteObject.indicator.waterproofingType?.let(referenceRemoteMapper::map),
                relief = remoteObject.indicator.relief,
                wastePlacementMaps = remoteObject.indicator.wastePlacementMaps?.let(wastePlacementMap::map)
                    .orEmpty()
                    .associateBy { it.id },
                groundWaterDepth = remoteObject.indicator.groundwaterDepth,
                canFlooding = remoteObject.indicator.canFlooding.orFalse(),
                hasWasteSealant = remoteObject.indicator.hasWasteSealant.orFalse(),
                sealantType = remoteObject.indicator.sealantType,
                sealantWeight = remoteObject.indicator.sealantWeight,
                sealantPhotos = mediaMapper.map(remoteObject.indicator.sealantPhotos.orEmpty())
            ),
            infrastructureSurvey = infrastructureSurvey
        )
    }

    private fun mapWasteRecyclingSurvey(
        remoteObject: RemoteVerificationObject,
        infrastructureSurvey: InfrastructureSurvey,
        productionSurvey: ProductionSurvey
    ): Survey.WasteRecyclingSurvey {

        val mainEquipment = remoteObject.equipment.getTechnicalEquipment(EquipmentKind.TECHNICAL_EQUIPMENT_MAIN)
        val secondaryEquipment = remoteObject.equipment.getTechnicalEquipment(EquipmentKind.TECHNICAL_EQUIPMENT_SECONDARY)

        return Survey.WasteRecyclingSurvey(
            technicalSurvey = TechnicalSurvey.WasteRecyclingTechnicalSurvey(
                objectArea = remoteObject.indicator.objectArea,
                objectPhotos = mediaMapper.map(remoteObject.indicator.objectPhotos.orEmpty()),
                productionArea = remoteObject.indicator.productionArea,
                productionPhotos = mediaMapper.map(remoteObject.indicator.productionPhotos.orEmpty()),
                wasteUnloadingArea = remoteObject.indicator.tkoWeightForLastYear,
                techSchema = mediaMapper.map(remoteObject.indicator.techSchema.orEmpty()),
                generalSchema = mediaMapper.map(remoteObject.indicator.generalSchema.orEmpty()),
                productionSchema = mediaMapper.map(remoteObject.indicator.productionSchema.orEmpty()),
                recyclingType = remoteObject.indicator.recyclingType?.let(referenceRemoteMapper::map),
                techProcessComment = remoteObject.indicator.techProcessComment,
                hasTemporaryWasteAccumulation = remoteObject.indicator.hasTemporaryWasteAccumulation.orFalse(),
                temporaryWasteArea = remoteObject.indicator.temporaryWasteArea,
                temporaryWastes = remoteObject.indicator.temporaryWastes,
                hasMainEquipment = remoteObject.hasCoreEquipment.orFalse(),
                mainEquipment = mainEquipment.associateBy { it.id }.k(),
                hasSecondaryEquipment = remoteObject.hasAuxiliaryEquipment.orFalse(),
                secondaryEquipment = secondaryEquipment.associateBy { it.id }.k(),
                receivedWastes = remoteObject.wasteInfo
                    ?.wasteTypes
                    .orEmpty()
                    .map { it.wasteType }
                    .map(referenceRemoteMapper::map),
                receivedWastesWeightThisYear = remoteObject.wasteInfo?.receivedWastesWeightThisYear,
                receivedWastesWeightLastYear = remoteObject.wasteInfo?.receivedWastesWeightLastYear
            ),
            infrastructureSurvey = infrastructureSurvey,
            productionSurvey = productionSurvey
        )
    }

    private fun mapWasteDisposalSurvey(
        remoteObject: RemoteVerificationObject,
        infrastructureSurvey: InfrastructureSurvey,
        productionSurvey: ProductionSurvey
    ): Survey.WasteDisposalSurvey {

        val mainEquipment = remoteObject.equipment.getTechnicalEquipment(EquipmentKind.TECHNICAL_EQUIPMENT_MAIN)
        val secondaryEquipment = remoteObject.equipment.getTechnicalEquipment(EquipmentKind.TECHNICAL_EQUIPMENT_SECONDARY)

        return Survey.WasteDisposalSurvey(
            technicalSurvey = TechnicalSurvey.WasteDisposalTechnicalSurvey(
                objectArea = remoteObject.indicator.objectArea,
                objectPhotos = mediaMapper.map(remoteObject.indicator.objectPhotos.orEmpty()),
                productionArea = remoteObject.indicator.productionArea,
                productionPhotos = mediaMapper.map(remoteObject.indicator.productionPhotos.orEmpty()),
                wasteUnloadingArea = remoteObject.indicator.tkoWeightForLastYear,
                techSchema = mediaMapper.map(remoteObject.indicator.techSchema.orEmpty()),
                generalSchema = mediaMapper.map(remoteObject.indicator.generalSchema.orEmpty()),
                productionSchema = mediaMapper.map(remoteObject.indicator.productionSchema.orEmpty()),
                techProcessComment = remoteObject.indicator.techProcessComment,
                hasThermalEnergyProduction = remoteObject.indicator.hasThermalRecycling.orFalse(),
                thermalEnergyProductionPower = remoteObject.indicator.thermalRecyclingPower,
                hasTemporaryWasteAccumulation = remoteObject.indicator.hasTemporaryWasteAccumulation.orFalse(),
                temporaryWasteArea = remoteObject.indicator.temporaryWasteArea,
                temporaryWastes = remoteObject.indicator.temporaryWastes,
                hasMainEquipment = remoteObject.hasCoreEquipment.orFalse(),
                mainEquipment = mainEquipment.associateBy { it.id }.k(),
                hasSecondaryEquipment = remoteObject.hasAuxiliaryEquipment.orFalse(),
                secondaryEquipment = secondaryEquipment.associateBy { it.id }.k(),
                receivedWastes = remoteObject.wasteInfo
                    ?.wasteTypes
                    .orEmpty()
                    .map { it.wasteType }
                    .map(referenceRemoteMapper::map),
            ),
            infrastructureSurvey = infrastructureSurvey,
            productionSurvey = productionSurvey
        )
    }

    private fun RemoteEquipment.getCommonEquipmentInfo(): CommonEquipmentInfo {
        return CommonEquipmentInfo(
            brand = brand.orEmpty(),
            manufacturer = manufacturer.orEmpty(),
            count = count,
            commonMediaInfo = CommonMediaInfo(
                photos = photos.orEmpty().let(mediaMapper::map),
                passport = passport?.firstOrNull()?.let(mediaMapper::map)
            )
        )
    }

    companion object {
        private val DEFAULT_OTHER_TYPE = Reference(
            id = "OTHER_REFERENCE_ID",
            type = ReferenceType.OTHER,
            name = "otherName"
        )
    }
}