package ru.ktsstudio.core_data_measurement_impl.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.10.2020.
 */
data class RemoteIdWrapper(
    @SerializedName("id")
    val id: String
)