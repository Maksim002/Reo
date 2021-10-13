package ru.ktsstudio.core_data_measurement_api.domain

import com.google.gson.annotations.SerializedName

enum class MeasurementMediaCategory {
    @SerializedName("PHOTO")
    PHOTO,
    @SerializedName("VIDEO")
    VIDEO,
    @SerializedName("ACT_PHOTO")
    ACT_PHOTO
}