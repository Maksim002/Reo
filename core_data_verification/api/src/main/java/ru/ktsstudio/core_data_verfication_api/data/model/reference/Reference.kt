package ru.ktsstudio.core_data_verfication_api.data.model.reference

import com.google.gson.annotations.SerializedName

/**
 * Created by Igor Park on 11/12/2020.
 */
data class Reference(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: ReferenceType,
    @SerializedName("name")
    val name: String
)