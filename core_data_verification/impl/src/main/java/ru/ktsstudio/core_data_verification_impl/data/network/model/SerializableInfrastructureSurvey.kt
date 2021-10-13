package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.Asu
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.AvailabilityInfo
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoringEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.Fences
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.InfrastructureEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.LightSystem
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.RoadNetwork
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SecurityCamera
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SecurityStation
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.SewagePlantEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.WeightControlEquipment

data class SerializableInfrastructureSurvey(
    @SerializedName("id")
    val id: String,
    @SerializedName("weightControl")
    val weightControlAvailabilityInfo: AvailabilityInfo,
    @SerializedName("wheelsWashing")
    val wheelsWashingAvailabilityInfo: AvailabilityInfo,
    @SerializedName("sewagePlant")
    val sewagePlantAvailabilityInfo: AvailabilityInfo,
    @SerializedName("radiationControl")
    val radiationControlAvailabilityInfo: AvailabilityInfo,
    @SerializedName("environmentMonitoring")
    val environmentMonitoringAvailabilityInfo: AvailabilityInfo?,

    @SerializedName("weightControlCount")
    val weightControlCount: Int?,
    @SerializedName("wheelsWashingPointCount")
    val wheelsWashingPointCount: Int?,
    @SerializedName("localTreatmentFacilitiesCount")
    val sewagePlantCount: Int?,
    @SerializedName("radiationControlCount")
    val radiationControlCount: Int?,
    @SerializedName("environmentMonitoringSystemCount")
    val environmentMonitoringSystemCount: Int?,

    @SerializedName("weightControlEquipment")
    val weightControlEquipment: List<WeightControlEquipment>,
    @SerializedName("wheelsWashingEquipment")
    val wheelsWashingEquipment: List<InfrastructureEquipment>,
    @SerializedName("sewagePlantEquipment")
    val sewagePlantEquipment: List<SewagePlantEquipment>,
    @SerializedName("radiationControlEquipment")
    val radiationControlEquipment: List<InfrastructureEquipment>,
    @SerializedName("environmentMonitoringEquipment")
    val environmentMonitoringEquipment: List<EnvironmentMonitoringEquipment>,

    @SerializedName("securityCamera")
    val securityCamera: SecurityCamera,
    @SerializedName("roadNetwork")
    val roadNetwork: RoadNetwork,
    @SerializedName("fences")
    val fences: Fences,
    @SerializedName("lightSystem")
    val lightSystem: LightSystem,
    @SerializedName("securityStation")
    val securityStation: SecurityStation,
    @SerializedName("asu")
    val asu: Asu
)
