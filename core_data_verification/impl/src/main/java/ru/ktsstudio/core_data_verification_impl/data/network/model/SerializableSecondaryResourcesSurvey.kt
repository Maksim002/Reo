package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources.SecondaryResourceType

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
data class SerializableSecondaryResourcesSurvey(
    @SerializedName("extractPercent")
    val extractPercent: Float?,
    @SerializedName("types")
    val types: List<SecondaryResourceType>
) {
    @SerializedName("surveySubtype")
    val surveySubtype: SurveySubtype = SurveySubtype.SECONDARY_RESOURCE
}