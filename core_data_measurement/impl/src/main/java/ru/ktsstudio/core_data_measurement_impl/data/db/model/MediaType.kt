package ru.ktsstudio.core_data_measurement_impl.data.db.model

import com.google.gson.annotations.SerializedName

enum class MediaType {
    @SerializedName("PHOTO")
    PHOTO,
    @SerializedName("VIDEO")
    VIDEO
}