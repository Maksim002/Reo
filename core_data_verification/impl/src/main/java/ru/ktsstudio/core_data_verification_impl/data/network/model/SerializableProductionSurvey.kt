package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.core_data_verfication_api.data.model.production.Product
import ru.ktsstudio.core_data_verfication_api.data.model.production.Service

data class SerializableProductionSurvey(
    @SerializedName("id")
    val id: String,
    @SerializedName("productCapacity")
    val productCapacity: Int?,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("services")
    val services: List<Service>
) {
    @SerializedName("surveySubtype")
    val surveySubtype: SurveySubtype = SurveySubtype.PRODUCTION
}