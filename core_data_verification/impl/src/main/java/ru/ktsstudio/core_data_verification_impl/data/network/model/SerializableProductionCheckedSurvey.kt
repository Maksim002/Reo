package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype

data class SerializableProductionCheckedSurvey(
    @SerializedName("productCapacity")
    val productCapacity: Boolean = false,
    @SerializedName("products")
    val products: Boolean = false,
    @SerializedName("services")
    val services: Boolean = false
) {
    @SerializedName("surveySubtype")
    val surveySubtype: SurveySubtype = SurveySubtype.PRODUCTION
}