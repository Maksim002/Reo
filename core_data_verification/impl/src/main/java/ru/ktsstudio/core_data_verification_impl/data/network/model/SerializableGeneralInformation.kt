package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 04/12/2020.
 */
data class SerializableGeneralInformation(
    @SerializedName("subject")
    val subject: Reference,
    @SerializedName("fiasAddress")
    val fiasAddress: RemoteFiasAddress?,
    @SerializedName("addressDescription")
    val addressDescription: String?,
    @SerializedName("name")
    val name: String
)
