package ru.ktsstudio.core_data_verfication_api.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Igor Park on 02/02/2021.
 */
enum class SurveyStatusType {
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS,

    @SerializedName("COMPLETED")
    COMPLETED,

    @SerializedName("NOT_STARTED")
    NOT_STARTED
}