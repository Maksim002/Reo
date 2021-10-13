package ru.ktsstudio.common.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.09.2020.
 */
data class RefreshTokenModel(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)