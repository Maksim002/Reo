package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.utilities.extensions.orFalse
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 21.12.2020.
 */
class InfrastructureSurveySendNetworkMapper @Inject constructor(
    private val referenceMapper: Mapper<Reference, RemoteReference>,
    private val mediaMapper: Mapper2<Media, Map<String, LocalMedia>, SerializableMedia>,
    private val environmentEquipmentMapper: EnvironmentEquipmentSendMapper,
    private val weightControlEquipmentMapper: WeightControlEquipmentSendMapper,
    private val infrastructureEquipmentSendMapper: InfrastructureEquipmentSendMapper,
    private val sewagePlantEquipmentSendMapper: SewagePlantEquipmentSendMapper
) {
    fun map(
        item: InfrastructureSurvey,
        localPathToMediaMap: Map<String, LocalMedia>
    ): RemoteInfrastructureSurvey = with(item) {
        return RemoteInfrastructureSurvey(
            id = id,
            hasWeightControl = weightControl.availabilityInfo.isAvailable,
            noWeightControlReason = weightControl.availabilityInfo.notAvailableReason,
            weightControlCount = weightControl.count,
            weightControls = weightControl.equipment.values.toList()
                .let { weightControlEquipmentMapper.map(it, localPathToMediaMap) },
            hasWheelsWashingPoint = wheelsWashing.availabilityInfo.isAvailable,
            noWheelsWashingPointReason = wheelsWashing.availabilityInfo.notAvailableReason,
            wheelsWashingPointCount = wheelsWashing.count,
            wheelsWashingPoints = wheelsWashing.equipment.values.toList()
                .let { infrastructureEquipmentSendMapper.map(it, localPathToMediaMap) },
            hasLocalSewagePlant = sewagePlant.availabilityInfo.isAvailable,
            noLocalSewagePlantReason = sewagePlant.availabilityInfo.notAvailableReason,
            localSewagePlantCount = sewagePlant.count,
            localTreatmentFacilities = sewagePlant.equipment.values.toList()
                .let { sewagePlantEquipmentSendMapper.map(it, localPathToMediaMap) },
            hasRadiationControl = radiationControl.availabilityInfo.isAvailable,
            noRadiationControlReason = radiationControl.availabilityInfo.notAvailableReason,
            radiationControlCount = radiationControl.count,
            radiationControls = radiationControl.equipment.values.toList()
                .let { infrastructureEquipmentSendMapper.map(it, localPathToMediaMap) },
            hasRoadNetwork = roadNetwork.availabilityInfo.isAvailable,
            noRoadNetworkReason = roadNetwork.availabilityInfo.notAvailableReason,
            road = RemoteInfrastructureObject(
                type = roadNetwork.roadCoverageType?.let(referenceMapper::map),
                length = roadNetwork.roadLength,
                schemePhotos = roadNetwork.schema
                    ?.let(::listOf)
                    ?.let { mediaMapper.map(it, localPathToMediaMap) }
            ),
            hasFences = fences.availabilityInfo.isAvailable,
            noFencesReason = fences.availabilityInfo.notAvailableReason,
            fence = RemoteInfrastructureObject(
                type = fences.fenceType?.let(referenceMapper::map),
                photos = fences.fencePhotos
                    .let { mediaMapper.map(it, localPathToMediaMap) }
            ),
            hasLights = lightSystem.availabilityInfo.isAvailable,
            noLightsReason = lightSystem.availabilityInfo.notAvailableReason,
            light = RemoteInfrastructureObject(
                type = lightSystem.lightSystemType?.let(referenceMapper::map)
            ),
            hasSecurity = securityStation.availabilityInfo.isAvailable,
            noSecurityReason = securityStation.availabilityInfo.notAvailableReason,
            security = RemoteInfrastructureObject(
                type = securityStation.securitySource?.let(referenceMapper::map),
                count = securityStation.securityStaffCount
            ),
            hasAsu = asu.availabilityInfo.isAvailable,
            noAsuReason = asu.availabilityInfo.notAvailableReason,
            asu = RemoteInfrastructureObject(
                functions = asu.systemFunctions,
                purpose = asu.systemName
            ),
            hasVideoEquipment = securityCamera.availabilityInfo.isAvailable,
            noVideoEquipmentReason = securityStation.availabilityInfo.notAvailableReason,
            videoEquipment = RemoteInfrastructureObject(
                count = securityCamera.count
            ),
            hasEnvironmentMonitoringSystem = environmentMonitoring?.availabilityInfo?.isAvailable.orFalse(),
            noEnvironmentMonitoringSystemReason = environmentMonitoring?.availabilityInfo
                ?.notAvailableReason,
            environmentMonitoringSystemCount = environmentMonitoring?.count,
            environmentMonitoringSystems = environmentMonitoring?.equipment
                ?.values
                ?.toList()
                ?.let(environmentEquipmentMapper::map)
                .orEmpty()
        )
    }
}