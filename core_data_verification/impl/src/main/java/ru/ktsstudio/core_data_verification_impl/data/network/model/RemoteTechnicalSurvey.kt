package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference

/**
 * @author Maxim Myalkin (MaxMyalkin) on 19.12.2020.
 */
data class RemoteTechnicalSurvey(
    @SerializedName("status")
    val status: RemoteSurveyStatus?,
    @SerializedName("startDate")
    val date: Long?
)