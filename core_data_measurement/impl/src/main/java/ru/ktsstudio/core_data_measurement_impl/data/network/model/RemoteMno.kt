package ru.ktsstudio.core_data_measurement_impl.data.network.model

import com.google.gson.annotations.SerializedName

data class RemoteMno(
    @SerializedName("mnoId") val id: String,
    @SerializedName("taskIds") val taskIds: List<RemoteIdWrapper>,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("containers") val containers: List<Container>,
    @SerializedName("sourceName") val sourceName: String,
    @SerializedName("sourceType") val sourceType: String,
    @SerializedName("category") val category: RemoteCategory,
    @SerializedName("subcategory") val subcategory: String,
    @SerializedName("unitType") val unitType: String,
    @SerializedName("unitQuantity") val unitQuantity: Double,
    @SerializedName("altUnitType") val altUnitType: String,
    @SerializedName("altUnitQuantity") val altUnitQuantity: Double,

    @SerializedName("federalDistrict") val federalDistrict: String?,
    @SerializedName("municipalDistrict") val municipalDistrict: String?,
    @SerializedName("region") val region: String?,
    @SerializedName("address") val address: String
) {
    data class Container(
        @SerializedName("containerId") val id: String,
        @SerializedName("containerName") val name: String,
        @SerializedName("containerType") val type: RemoteRegister.ContainerType,
        @SerializedName("containerVolume") val volume: Float,
        @SerializedName("scheduleType") val scheduleType: String?
    )
}