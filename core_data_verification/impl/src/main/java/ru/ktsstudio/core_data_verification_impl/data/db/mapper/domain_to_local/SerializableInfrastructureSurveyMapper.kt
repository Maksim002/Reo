package ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureSurvey
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableInfrastructureSurvey
import javax.inject.Inject

/**
 * Created by Igor Park on 11/12/2020.
 */
class SerializableInfrastructureSurveyMapper @Inject constructor() : Mapper<
    InfrastructureSurvey,
    SerializableInfrastructureSurvey
    > {
    override fun map(item: InfrastructureSurvey): SerializableInfrastructureSurvey {
        return with(item) {
            SerializableInfrastructureSurvey(
                id = id,
                weightControlAvailabilityInfo = weightControl.availabilityInfo,
                weightControlCount = weightControl.count,
                weightControlEquipment = weightControl.equipment.values.toList(),
                wheelsWashingAvailabilityInfo = wheelsWashing.availabilityInfo,
                wheelsWashingPointCount = wheelsWashing.count,
                wheelsWashingEquipment = wheelsWashing.equipment.values.toList(),
                sewagePlantAvailabilityInfo = sewagePlant.availabilityInfo,
                sewagePlantCount = sewagePlant.count,
                sewagePlantEquipment = sewagePlant.equipment.values.toList(),
                radiationControlAvailabilityInfo = radiationControl.availabilityInfo,
                radiationControlCount = radiationControl.count,
                radiationControlEquipment = radiationControl.equipment.values.toList(),
                environmentMonitoringAvailabilityInfo = environmentMonitoring?.availabilityInfo,
                environmentMonitoringSystemCount = environmentMonitoring?.count,
                environmentMonitoringEquipment = environmentMonitoring?.equipment
                    ?.values
                    ?.toList()
                    .orEmpty(),
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