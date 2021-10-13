package ru.ktsstudio.core_data_verfication_api.data.model.equipment

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

@optics
data class AdditionalEquipment(
    @SerializedName("id")
    val id: String,
    @SerializedName("common")
    val commonEquipmentInfo: CommonEquipmentInfo,
    @SerializedName("type")
    val type: Reference?,
    @SerializedName("otherName")
    val otherName: String?,
    @SerializedName("created")
    val created: Instant?
) {
    companion object {
        fun createEmpty(id: String): AdditionalEquipment {
            return AdditionalEquipment(
                id = id,
                commonEquipmentInfo = CommonEquipmentInfo.EMPTY,
                type = null,
                otherName = null,
                created = Instant.now()
            )
        }
    }
}