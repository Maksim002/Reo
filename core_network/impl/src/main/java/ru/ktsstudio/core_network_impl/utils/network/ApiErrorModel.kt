package ru.ktsstudio.core_network_impl.utils.network

import com.google.gson.annotations.SerializedName

data class ApiErrorModel(
    @SerializedName("error")
    val error: String?,
    @SerializedName("error_description")
    val errorDescription: String?
) {
    val fullError: String
        get() = "error=$error, description=$errorDescription"
}
