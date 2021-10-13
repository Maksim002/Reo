package ru.ktsstudio.common.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
data class RemoteSettings(
    @SerializedName("email") val supportEmail: String?,
    @SerializedName("phone") val supportPhoneNumber: String?
)