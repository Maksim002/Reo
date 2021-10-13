package ru.ktsstudio.common.data.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Igor Park on 19/11/2020.
 */
enum class LocalModelState {
    @SerializedName("PENDING")
    PENDING,
    @SerializedName("ERROR")
    ERROR,
    @SerializedName("SUCCESS")
    SUCCESS
}