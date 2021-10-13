package ru.ktsstudio.core_data_verfication_api.data.model.reference

import com.google.gson.annotations.SerializedName

/**
 * Created by Igor Park on 11/12/2020.
 */
data class RemoteReference(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: RemoteType,
    @SerializedName("value")
    val value: String
)

data class RemoteType(
    @SerializedName("name")
    val name: ReferenceType?,
    @SerializedName("displayName")
    val displayName: String
)