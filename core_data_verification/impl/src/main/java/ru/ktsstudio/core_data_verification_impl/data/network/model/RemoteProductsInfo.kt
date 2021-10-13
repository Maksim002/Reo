package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 28.12.2020.
 */
data class RemoteProductsInfo(
    @SerializedName("id")
    val id: String,
    @SerializedName("totalCountPerYear")
    val totalCountPerYear: Int?,
    @SerializedName("products")
    val products: List<RemoteProduct>?,
    @SerializedName("providedServices")
    val providedServices: List<RemoteService>?
)


data class RemoteProduct(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("volume")
    val volume: Int?,
    @SerializedName("photos")
    val photos: List<SerializableMedia>?
)

data class RemoteService(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("volume")
    val volume: Int?,
)
