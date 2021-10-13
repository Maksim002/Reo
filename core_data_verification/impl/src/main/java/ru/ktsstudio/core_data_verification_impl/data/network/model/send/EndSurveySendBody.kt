package ru.ktsstudio.core_data_verification_impl.data.network.model.send

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 21.12.2020.
 */
data class EndSurveySendBody(
    @SerializedName("objectId")
    val objectId: String
)