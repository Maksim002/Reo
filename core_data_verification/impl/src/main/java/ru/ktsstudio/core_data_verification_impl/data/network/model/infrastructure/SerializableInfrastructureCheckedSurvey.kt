package ru.ktsstudio.core_data_verification_impl.data.network.model.infrastructure

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype

data class SerializableInfrastructureCheckedSurvey(
    @SerializedName("weightControl") val weightControl: Boolean,
    @SerializedName("wheelsWashing") val wheelsWashing: Boolean,
    @SerializedName("sewagePlant") val sewagePlant: Boolean,
    @SerializedName("radiationControl") val radiationControl: Boolean,
    @SerializedName("securityCamera") val securityCamera: Boolean,
    @SerializedName("roadNetwork") val roadNetwork: Boolean,
    @SerializedName("fences") val fences: Boolean,
    @SerializedName("lightSystem") val lightSystem: Boolean,
    @SerializedName("securityStation") val securityStation: Boolean,
    @SerializedName("asu") val asu: Boolean,
    @SerializedName("environmentMonitoring") val environmentMonitoring: Boolean?
) {
    @SerializedName("surveySubtype")
    val surveySubtype: SurveySubtype = SurveySubtype.INFRASTRUCTURAL
}