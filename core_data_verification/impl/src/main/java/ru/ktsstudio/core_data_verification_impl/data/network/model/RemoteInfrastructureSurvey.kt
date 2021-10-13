package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference

/**
 * Created by Igor Park on 11/12/2020.
 */
data class RemoteInfrastructureSurvey(
    @SerializedName("id")
    val id: String,

    @SerializedName("hasWeightControl")
    val hasWeightControl: Boolean,
    @SerializedName("noWeightControlReason")
    val noWeightControlReason: String?,
    @SerializedName("weightControlCount")
    val weightControlCount: Int?,
    @SerializedName("weightControls")
    val weightControls: List<RemoteInfrastructureObject>?,

    @SerializedName("hasWheelsWashingPoint")
    val hasWheelsWashingPoint: Boolean,
    @SerializedName("noWheelsWashingPointReason")
    val noWheelsWashingPointReason: String?,
    @SerializedName("wheelsWashingPointCount")
    val wheelsWashingPointCount: Int?,
    @SerializedName("wheelsWashingPoints")
    val wheelsWashingPoints: List<RemoteInfrastructureObject>?,

    @SerializedName("hasLocalTreatmentFacilities")
    val hasLocalSewagePlant: Boolean,
    @SerializedName("noLocalTreatmentFacilitiesReason")
    val noLocalSewagePlantReason: String?,
    @SerializedName("localTreatmentFacilitiesCount")
    val localSewagePlantCount: Int?,
    @SerializedName("localTreatmentFacilities")
    val localTreatmentFacilities: List<RemoteInfrastructureObject>?,

    @SerializedName("hasRadiationControl")
    val hasRadiationControl: Boolean,
    @SerializedName("noRadiationControlReason")
    val noRadiationControlReason: String?,
    @SerializedName("radiationControlCount")
    val radiationControlCount: Int?,
    @SerializedName("radiationControls")
    val radiationControls: List<RemoteInfrastructureObject>?,

    @SerializedName("hasRoads")
    val hasRoadNetwork: Boolean,
    @SerializedName("noRoadsReason")
    val noRoadNetworkReason: String?,
    @SerializedName("road")
    val road: RemoteInfrastructureObject?,

    @SerializedName("hasFences")
    val hasFences: Boolean,
    @SerializedName("noFencesReason")
    val noFencesReason: String?,
    @SerializedName("fence")
    val fence: RemoteInfrastructureObject?,

    @SerializedName("hasLights")
    val hasLights: Boolean,
    @SerializedName("noLightsReason")
    val noLightsReason: String?,
    @SerializedName("light")
    val light: RemoteInfrastructureObject?,

    @SerializedName("hasSecurity")
    val hasSecurity: Boolean,
    @SerializedName("noSecurityReason")
    val noSecurityReason: String?,
    @SerializedName("security")
    val security: RemoteInfrastructureObject?,

    @SerializedName("hasAsu")
    val hasAsu: Boolean,
    @SerializedName("noAsuReason")
    val noAsuReason: String?,
    @SerializedName("asu")
    val asu: RemoteInfrastructureObject?,

    @SerializedName("hasVideoEquipment")
    val hasVideoEquipment: Boolean,
    @SerializedName("noVideoEquipmentReason")
    val noVideoEquipmentReason: String?,
    @SerializedName("videoEquipment")
    val videoEquipment: RemoteInfrastructureObject?,

    @SerializedName("hasEnvironmentMonitoringSystem")
    val hasEnvironmentMonitoringSystem: Boolean?,
    @SerializedName("noEnvironmentMonitoringSystemReason")
    val noEnvironmentMonitoringSystemReason: String?,
    @SerializedName("environmentMonitoringSystemCount")
    val environmentMonitoringSystemCount: Int?,
    @SerializedName("environmentMonitoringSystems")
    val environmentMonitoringSystems: List<RemoteInfrastructureObject>?
)

data class RemoteInfrastructureObject(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("brand")
    val brand: String? = null,
    @SerializedName("manufacturer")
    val manufacturer: String? = null,
    @SerializedName("length")
    val length: Float? = null,
    @SerializedName("photos")
    val photos: List<SerializableMedia>? = null,
    @SerializedName("type")
    val type: RemoteReference? = null,
    @SerializedName("capacity")
    val capacity: Float? = null,
    @SerializedName("passportPhotos")
    val passport: List<SerializableMedia>? = null,
    @SerializedName("schemePhotos")
    val schemePhotos: List<SerializableMedia>? = null,
    @SerializedName("purpose")
    val purpose: String? = null,
    @SerializedName("functions")
    val functions: String? = null
)