package ru.ktsstudio.core_data_verfication_api.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Igor Park on 04/12/2020.
 */
enum class SurveySubtype {
    @SerializedName("GENERAL")
    GENERAL,
    @SerializedName("SCHEDULE")
    SCHEDULE,
    @SerializedName("TECHNICAL")
    TECHNICAL,
    @SerializedName("INFRASTRUCTURAL")
    INFRASTRUCTURAL,
    @SerializedName("PRODUCTION")
    PRODUCTION,
    @SerializedName("EQUIPMENT")
    EQUIPMENT,
    @SerializedName("SECONDARY_RESOURCE")
    SECONDARY_RESOURCE
}