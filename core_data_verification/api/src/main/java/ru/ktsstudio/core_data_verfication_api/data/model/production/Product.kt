package ru.ktsstudio.core_data_verfication_api.data.model.production

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.Media

@optics
data class Product(
    @SerializedName("id")
    val id: String,
    @SerializedName("photos")
    val photos: List<Media>,
    @SerializedName("output")
    val output: Int?,
    @SerializedName("name")
    val name: String?
) {
    companion object {
        fun createEmpty(id: String): Product {
            return Product(
                id = id,
                photos = emptyList(),
                output = null,
                name = null
            )
        }
    }
}