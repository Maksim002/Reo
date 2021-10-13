package ru.ktsstudio.core_data_verfication_api.data.model.infrastructure

import arrow.optics.optics

@optics
data class InfrastructureCheckedSurvey(
    val weightControl: Boolean = false,
    val wheelsWashing: Boolean = false,
    val sewagePlant: Boolean = false,
    val radiationControl: Boolean = false,
    val securityCamera: Boolean = false,
    val roadNetwork: Boolean = false,
    val fences: Boolean = false,
    val lightSystem: Boolean = false,
    val securityStation: Boolean = false,
    val asu: Boolean = false,
    val environmentMonitoring: Boolean? = null
) {
    companion object
}