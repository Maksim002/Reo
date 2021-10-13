package ru.ktsstudio.core_data_measurement_impl.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.10.2020.
 */
data class RemoteCategory(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)