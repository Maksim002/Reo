package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Ovchinnikov on 10.11.2020.
 */
data class RemoteRegister(
    @SerializedName("status") val statuses: List<Status>,
    @SerializedName("objectStatus") val objectStatuses: List<ObjectStatus>,
    @SerializedName("objectType") val objectTypes: List<ObjectType>,
    @SerializedName("wasteType") val wasteTypes: List<WasteType>,
    @SerializedName("landCategories") val landCategories: List<LandCategories>,
    @SerializedName("equipment") val equipment: Equipment
) {

    data class Status(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("order") val order: Int,
    )

    data class ObjectStatus(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String
    )

    data class ObjectType(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String
    )

    data class WasteType(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String
    )

    data class LandCategories(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String
    )

    data class Equipment(
        @SerializedName("manufacturer") val manufacturer: List<Manufacturer>,
        @SerializedName("type") val type: List<Type>,
        @SerializedName("brand") val brand: List<Brand>
    ) {
        data class Manufacturer(
            @SerializedName("id") val id: Long,
            @SerializedName("name") val name: String
        )

        data class Type(
            @SerializedName("id") val id: Long,
            @SerializedName("name") val name: String
        )

        data class Brand(
            @SerializedName("id") val id: Long,
            @SerializedName("name") val name: String
        )
    }
}