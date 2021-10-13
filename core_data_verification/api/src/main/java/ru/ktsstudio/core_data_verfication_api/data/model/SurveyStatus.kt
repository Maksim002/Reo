package ru.ktsstudio.core_data_verfication_api.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Igor Park on 02/02/2021.
 */
data class SurveyStatus(
    @SerializedName("statusName")
    val name: String?,
    @SerializedName("statusType")
    val type: SurveyStatusType
)