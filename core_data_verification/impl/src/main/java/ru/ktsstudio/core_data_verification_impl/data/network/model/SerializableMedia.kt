package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */

data class SerializableMedia(
    @SerializedName("id")
    val remoteId: String?,
    @SerializedName("localPath")
    val localPath: String?,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("longitude")
    val longitude: String?,
    @SerializedName("created")
    val date: Long
)
