package ru.ktsstudio.core_data_verfication_api.data.model.secondary_material_resources

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonConveyorInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.ServingConveyor
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.12.2020.
 */
@optics
data class SecondaryResourceType(
    @SerializedName("id")
    val id: String,
    @SerializedName("reference")
    val reference: Reference?,
    @SerializedName("otherName")
    val otherName: String?,
    @SerializedName("percent")
    val percent: Float?
) {
    companion object {
        fun createEmpty(id: String): SecondaryResourceType {
            return SecondaryResourceType(
                id = id,
                reference = null,
                percent = null,
                otherName = null
            )
        }
    }
}