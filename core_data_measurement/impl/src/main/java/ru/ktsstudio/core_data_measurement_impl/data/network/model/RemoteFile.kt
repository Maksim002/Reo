package ru.ktsstudio.core_data_measurement_impl.data.network.model

import com.google.gson.annotations.SerializedName

data class RemoteFile(
    @SerializedName("id")
    val id: String,
    @SerializedName("mime")
    val mime: String,
    @SerializedName("name")
    val name: String
)