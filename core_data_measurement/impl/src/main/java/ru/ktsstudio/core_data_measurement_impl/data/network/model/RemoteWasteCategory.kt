package ru.ktsstudio.core_data_measurement_impl.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Igor Park on 29/10/2020.
 */
data class RemoteWasteCategory(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)