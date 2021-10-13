package ru.ktsstudio.core_data_verification_impl.data.network.model

import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Conveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Press
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.Separator
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ServingConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.SortConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.SurveySubtype
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.AdditionalEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.BagBreakerConveyor

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
data class SerializableEquipmentSurvey(
    @SerializedName("servingConveyors")
    val servingConveyors: List<ServingConveyor>,
    @SerializedName("sortConveyors")
    val sortConveyors: List<SortConveyor>,
    @SerializedName("reverseConveyors")
    val reverseConveyors: List<Conveyor>,
    @SerializedName("pressConveyors")
    val pressConveyors: List<Conveyor>,
    @SerializedName("otherConveyors")
    val otherConveyors: List<Conveyor>,
    @SerializedName("bagBreakers")
    val bagBreakers: List<BagBreakerConveyor>,
    @SerializedName("separators")
    val separators: List<Separator>,
    @SerializedName("presses")
    val presses: List<Press>,
    @SerializedName("additional")
    val additionalEquipment: List<AdditionalEquipment>
) {
    @SerializedName("surveySubtype")
    val surveySubtype: SurveySubtype = SurveySubtype.EQUIPMENT
}