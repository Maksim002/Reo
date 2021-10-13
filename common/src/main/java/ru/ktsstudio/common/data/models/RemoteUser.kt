package ru.ktsstudio.common.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 29.09.2020.
 */
data class RemoteUser(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String
)