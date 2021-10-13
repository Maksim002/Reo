package ru.ktsstudio.core_data_verification_impl.data.db.mapper.local_to_domain

import arrow.core.k
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoring
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.RadiationControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlant
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControl
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WheelsWashing
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableInfrastructureSurvey
import javax.inject.Inject

/**
 * Created by Igor Park on 11/12/2020.
 */
class InfrastructureSurveyMapper @Inject constructor() : Mapper<
    SerializableInfrastructureSurvey,
    InfrastructureSurvey
    > {
    override fun map(item: SerializableInfrastructureSurvey): InfrastructureSurvey {
        return with(item) {
            InfrastructureSurvey(
                id = id,
                weightControl = WeightControl(
                    availabilityInfo = weightControlAvailabilityInfo,
                    count = weightControlCount,
                    equipment = weightControlEquipment.associateBy { it.id }.k()
                ),
                wheelsWashing = WheelsWashing(
                    availabilityInfo = wheelsWashingAvailabilityInfo,
                    count = wheelsWashingPointCount,
                    equipment = wheelsWashingEquipment.associateBy { it.id }.k()
                ),
                sewagePlant = SewagePlant(
                    availabilityInfo = sewagePlantAvailabilityInfo,
                    count = sewagePlantCount,
                    equipment = sewagePlantEquipment.associateBy { it.id }.k()
                ),
                radiationControl = RadiationControl(
                    availabilityInfo = radiationControlAvailabilityInfo,
                    count = radiationControlCount,
                    equipment = radiationControlEquipment.associateBy { it.id }.k()
                ),
                environmentMonitoring = environmentMonitoringAvailabilityInfo?.let {
                    EnvironmentMonitoring(
                        availabilityInfo = environmentMonitoringAvailabilityInfo,
                        count = environmentMonitoringSystemCount,
                        equipment = environmentMonitoringEquipment.associateBy { it.id }.k()
                    )
                },
                securityCamera = securityCamera,
                roadNetwork = roadNetwork,
                fences = fences,
                lightSystem = lightSystem,
                securityStation = securityStation,
                asu = asu
            )
        }
    }
}