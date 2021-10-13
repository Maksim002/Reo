package ru.ktsstudio.core_data_verfication_api.data.model.infrastructure

import arrow.optics.optics

@optics
data class InfrastructureSurvey(
    val id: String,
    val weightControl: WeightControl,
    val wheelsWashing: WheelsWashing,
    val sewagePlant: SewagePlant,
    val radiationControl: RadiationControl,
    val securityCamera: SecurityCamera,
    val roadNetwork: RoadNetwork,
    val fences: Fences,
    val lightSystem: LightSystem,
    val securityStation: SecurityStation,
    val asu: Asu,
    val environmentMonitoring: EnvironmentMonitoring?
) {
    companion object
}