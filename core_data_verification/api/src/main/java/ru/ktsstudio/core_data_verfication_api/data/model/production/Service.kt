package ru.ktsstudio.core_data_verfication_api.data.model.production

import arrow.optics.optics
import com.google.gson.annotations.SerializedName

@optics
data class Service(
    @SerializedName("id")
    val id: String,
    @SerializedName("output")
    val output: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("unit")
    val unit: String?
) {
    companion object {
        fun createEmpty(id: String): Service {
            return Service(
                id = id,
                output = null,
                name = null,
                unit = null
            )
        }
    }
}