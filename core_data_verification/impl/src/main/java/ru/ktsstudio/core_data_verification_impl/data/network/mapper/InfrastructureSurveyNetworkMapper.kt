package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import arrow.core.k
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectType
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.Asu
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.AvailabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoring
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoringEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.Fences
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.LightSystem
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.RadiationControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.RoadNetwork
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SecurityCamera
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SecurityStation
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlant
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlantEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControlEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WheelsWashing
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.utilities.extensions.orFalse
import javax.inject.Inject

/**
 * Created by Igor Park on 11/12/2020.
 */
class InfrastructureSurveyNetworkMapper @Inject constructor(
    private val referenceRemoteMapper: Mapper<RemoteReference, Reference>,
    private val mediaMapper: Mapper<SerializableMedia, Media>,
    private val weightControlEquipmentMapper: Mapper<RemoteInfrastructureObject, WeightControlEquipment>,
    private val infrastructureEquipmentMapper: Mapper<RemoteInfrastructureObject, InfrastructureEquipment>,
    private val sewagePlantEquipmentMapper: Mapper<RemoteInfrastructureObject, SewagePlantEquipment>,
    private val environmentMonitoringEquipmentMapper: Mapper<RemoteInfrastructureObject, EnvironmentMonitoringEquipment>
) : Mapper2<
    RemoteVerificationObject,
    VerificationObjectType,
    InfrastructureSurvey> {

    override fun map(
        item1: RemoteVerificationObject,
        item2: VerificationObjectType
    ): InfrastructureSurvey = with(item1.infrastructureSurvey) {
        val weightControlEquipment = weightControlEquipmentMapper.map(weightControls.orEmpty())
        val wheelWashingEquipment = infrastructureEquipmentMapper.map(wheelsWashingPoints.orEmpty())
        val sewagePlantEquipment =
            sewagePlantEquipmentMapper.map(localTreatmentFacilities.orEmpty())
        val radiationControlEquipment =
            infrastructureEquipmentMapper.map(radiationControls.orEmpty())
        val environmentMonitoringEquipment =
            environmentMonitoringEquipmentMapper.map(environmentMonitoringSystems.orEmpty())
        return InfrastructureSurvey(
            id = id,
            weightControl = WeightControl(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasWeightControl,
                    notAvailableReason = noWeightControlReason
                ),
                count = weightControlCount,
                equipment = weightControlEquipment.associateBy { it.id }.k()
            ),
            wheelsWashing = WheelsWashing(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasWheelsWashingPoint,
                    notAvailableReason = noWheelsWashingPointReason
                ),
                count = wheelsWashingPointCount,
                equipment = wheelWashingEquipment.associateBy { it.id }.k()
            ),
            sewagePlant = SewagePlant(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasLocalSewagePlant,
                    notAvailableReason = noLocalSewagePlantReason
                ),
                count = localSewagePlantCount,
                equipment = sewagePlantEquipment.associateBy { it.id }.k()
            ),
            radiationControl = RadiationControl(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasRadiationControl,
                    notAvailableReason = noRadiationControlReason
                ),
                count = weightControlCount,
                equipment = radiationControlEquipment.associateBy { it.id }.k()
            ),
            securityCamera = SecurityCamera(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasVideoEquipment,
                    notAvailableReason = noVideoEquipmentReason
                ),
                count = videoEquipment?.count
            ),
            roadNetwork = RoadNetwork(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasVideoEquipment,
                    notAvailableReason = noVideoEquipmentReason
                ),
                roadCoverageType = road?.type?.let(referenceRemoteMapper::map),
                roadLength = road?.length,
                schema = road?.schemePhotos?.firstOrNull()?.let(mediaMapper::map)
            ),
            fences = Fences(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasFences,
                    notAvailableReason = noFencesReason
                ),
                fenceType = fence?.type?.let(referenceRemoteMapper::map),
                fencePhotos = fence?.photos?.let(mediaMapper::map).orEmpty()
            ),
            lightSystem = LightSystem(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasLights,
                    notAvailableReason = noLightsReason
                ),
                lightSystemType = light?.type?.let(referenceRemoteMapper::map)
            ),
            securityStation = SecurityStation(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasSecurity,
                    notAvailableReason = noSecurityReason
                ),
                securitySource = security?.type?.let(referenceRemoteMapper::map),
                securityStaffCount = security?.count
            ),
            asu = Asu(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasSecurity,
                    notAvailableReason = noSecurityReason
                ),
                systemFunctions = asu?.functions,
                systemName = asu?.purpose
            ),
            environmentMonitoring = EnvironmentMonitoring(
                availabilityInfo = AvailabilityInfo(
                    isAvailable = hasEnvironmentMonitoringSystem.orFalse(),
                    notAvailableReason = noEnvironmentMonitoringSystemReason
                ),
                count = environmentMonitoringSystemCount,
                equipment = environmentMonitoringEquipment.associateBy { it.id }.k()
            )
        )
    }
}