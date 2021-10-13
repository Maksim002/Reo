package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import arrow.core.MapK
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.FiasAddress
import ru.ktsstudio.core_data_verfication_api.data.model.ProductionSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.schedule.ScheduleSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.Survey
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.EquipmentKind
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourcesSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.technical.TechnicalSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObjectWithRelation
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.getIndexes
import ru.ktsstudio.core_data_verification_impl.data.network.mapper.objectTypeToIdMap
import ru.ktsstudio.core_data_verification_impl.data.network.model.GpsPointToSend
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteEquipment
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteFiasAddress
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteProductsInfo
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteSecondaryResources
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteSurveyStatus
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteTechnicalSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWasteInfo
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWasteType
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableObjectWorkSchedule
import ru.ktsstudio.utilities.extensions.orDefault
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 20.12.2020.
 */
class ObjectSendNetworkMapper @Inject constructor(
    private val scheduleSurveyMapper: Mapper<ScheduleSurvey, SerializableObjectWorkSchedule>,
    private val equipmentSendMapper: EquipmentSurveySendMapper,
    private val technicalEquipmentMapper: TechnicalEquipmentMapper,
    private val referenceMapper: Mapper<Reference, RemoteReference>,
    private val fiasMapper: Mapper<FiasAddress, RemoteFiasAddress>,
    private val technicalSurveyMapper: TechnicalSurveySendNetworkMapper,
    private val infrastructureSurveyMapper: InfrastructureSurveySendNetworkMapper,
    private val verifiedFieldsMapper: VerifiedFieldsToSendMapper,
    private val secondaryResourceMapper: Mapper<SecondaryResourcesSurvey, RemoteSecondaryResources>,
    private val productionSurveyMapper: Mapper2<ProductionSurvey, Map<String, LocalMedia>, RemoteProductsInfo>,
    private val wastePlacementSendMapper: Mapper<WastePlacementMap, RemoteWastePlacementMap>,
    private val gpsPointSendMapper: Mapper<GpsPoint, GpsPointToSend>,
    private val wasteInfoSendNetworkMapper: WasteInfoSendNetworkMapper
) {

    fun map(
        item1: List<LocalVerificationObjectWithRelation>,
        item2: List<Reference>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteVerificationObject> {
        return item1.map {
            mapSingle(it, item2, localPathToMediaMap)
        }
    }

    private fun mapSingle(
        item1: LocalVerificationObjectWithRelation,
        item2: List<Reference>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): RemoteVerificationObject {
        val type = item2.find {
            it.type == ReferenceType.valueOf(OBJECT_TYPE_REFERENCE) &&
                it.id == objectTypeToIdMap[item1.verificationObject.type]
        }.requireNotNull()

        val equipment = getEquipment(item1.verificationObject.survey, item2, localPathToMediaMap)
        val production = getProductionInfo(item1.verificationObject.survey, localPathToMediaMap)
        val secondaryResources = getSecondaryResources(item1.verificationObject.survey)
        val wastePlacement = getWastePlacement(item1.verificationObject.survey)
        val (hasMainEquipment, hasSecondaryEquipment) = getHasEquipmentFlags(item1.verificationObject.survey)
        val wasteTypes = when (val techSurvey = item1.verificationObject.survey.technicalSurvey) {
            is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> techSurvey.receivedWastes
            is TechnicalSurvey.WasteDisposalTechnicalSurvey -> techSurvey.receivedWastes
            else -> emptyList()
        }
        val gpsPointToSend = gpsPointSendMapper.map(item1.verificationObject.location)

        return RemoteVerificationObject(
            id = item1.verificationObject.id,
            name = item1.verificationObject.generalInformation.name,
            latitude = gpsPointToSend.lat,
            longitude = gpsPointToSend.lng,
            type = referenceMapper.map(type),
            workSchedule = scheduleSurveyMapper.map(item1.verificationObject.scheduleSurvey),
            addressDescription = item1.verificationObject.address,
            technicalSurvey = RemoteTechnicalSurvey(
                status = item1.verificationObject.status?.let {
                    RemoteSurveyStatus(
                        displayName = item1.verificationObject.status.name,
                        statusType = item1.verificationObject.status.type
                    )
                },
                date = item1.verificationObject.date.toEpochMilli().takeIf { it > 0 }
            ),
            objectStatus = item1.verificationObject
                .objectStatus
                .takeIf { it?.type != ReferenceType.OTHER }
                ?.let(referenceMapper::map),
            otherObjectStatusName = item1.verificationObject.otherObjectStatusName,
            infrastructureSurvey = infrastructureSurveyMapper.map(
                item1.verificationObject.survey.infrastructureSurvey,
                localPathToMediaMap
            ),
            fiasAddress = item1.verificationObject.generalInformation.fiasAddress?.let(fiasMapper::map),
            subject = referenceMapper.map(item1.verificationObject.generalInformation.subject),
            verifiedFields = verifiedFieldsMapper.map(
                equipment = equipment,
                production = production,
                secondaryResources = secondaryResources,
                wastePlacement = wastePlacement,
                receivedWasteTypePositions = wasteTypes.getIndexes(),
                checkedSurvey = item1.checkedSurveyHolder.checkedSurvey,
                infrastructureSurvey = item1.verificationObject.survey.infrastructureSurvey,
                techSurvey = item1.verificationObject.survey.technicalSurvey
            ),
            indicator = technicalSurveyMapper.map(
                item1.verificationObject.survey.technicalSurvey,
                localPathToMediaMap
            ),
            equipment = equipment,
            secondaryResources = secondaryResources,
            productionInfo = production,
            hasCoreEquipment = hasMainEquipment,
            hasAuxiliaryEquipment = hasSecondaryEquipment,
            wasteInfo = wasteInfoSendNetworkMapper.map(wasteTypes, item1.verificationObject.survey.technicalSurvey)
        )
    }

    private fun getEquipment(
        survey: Survey,
        references: List<Reference>,
        localPathToMediaMap: Map<String, LocalMedia>
    ): List<RemoteEquipment> {

        fun getTechnicalEquipmentToSend(
            mainEquipment: MapK<String, TechnicalEquipment>,
            secondaryEquipment: MapK<String, TechnicalEquipment>
        ): List<RemoteEquipment> {
            val main = technicalEquipmentMapper.map(
                EquipmentKind.TECHNICAL_EQUIPMENT_MAIN,
                mainEquipment.values.toList(),
                references,
                localPathToMediaMap
            )
            val secondary = technicalEquipmentMapper.map(
                EquipmentKind.TECHNICAL_EQUIPMENT_SECONDARY,
                secondaryEquipment.values.toList(),
                references,
                localPathToMediaMap
            )

            return (main + secondary).sortedBy { it.created.orDefault(0) }
        }

        return when (survey) {
            is Survey.WasteTreatmentSurvey -> {
                equipmentSendMapper.map(survey.equipmentSurvey, references, localPathToMediaMap)
            }
            is Survey.WasteRecyclingSurvey -> {
                getTechnicalEquipmentToSend(
                    survey.technicalSurvey.mainEquipment,
                    survey.technicalSurvey.secondaryEquipment
                )
            }
            is Survey.WasteDisposalSurvey -> {
                getTechnicalEquipmentToSend(
                    survey.technicalSurvey.mainEquipment,
                    survey.technicalSurvey.secondaryEquipment
                )
            }
            else -> emptyList()
        }
    }

    private fun getSecondaryResources(survey: Survey): RemoteSecondaryResources? {
        return when (survey) {
            is Survey.WasteTreatmentSurvey -> secondaryResourceMapper.map(survey.secondaryResourcesSurvey)
            else -> null
        }
    }

    private fun getHasEquipmentFlags(survey: Survey): Pair<Boolean?, Boolean?> {
        return when (val technicalSurvey = survey.technicalSurvey) {
            is TechnicalSurvey.WasteRecyclingTechnicalSurvey -> {
                technicalSurvey.hasMainEquipment to technicalSurvey.hasSecondaryEquipment
            }
            is TechnicalSurvey.WasteDisposalTechnicalSurvey -> {
                technicalSurvey.hasMainEquipment to technicalSurvey.hasSecondaryEquipment
            }
            else -> null to null
        }
    }

    private fun getProductionInfo(
        survey: Survey,
        mediaLocalIdToRemoteIdMap: Map<String, LocalMedia>
    ): RemoteProductsInfo? {
        return when (survey) {
            is Survey.WasteDisposalSurvey -> productionSurveyMapper.map(
                survey.productionSurvey,
                mediaLocalIdToRemoteIdMap
            )
            is Survey.WasteRecyclingSurvey -> productionSurveyMapper.map(
                survey.productionSurvey,
                mediaLocalIdToRemoteIdMap
            )
            else -> null
        }
    }

    private fun getWastePlacement(survey: Survey): List<RemoteWastePlacementMap> {
        return if (survey is Survey.WastePlacementSurvey) {
            wastePlacementSendMapper.map(survey.technicalSurvey.wastePlacementMaps.values.toList())
        } else {
            emptyList()
        }
    }

    companion object {
        private const val OBJECT_TYPE_REFERENCE = "WASTE_MANAGEMENT_TYPE"
    }
}