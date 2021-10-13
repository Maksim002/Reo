package ru.ktsstudio.core_data_verfication_api.data.model.technical

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import ru.ktsstudio.core_data_verfication_api.data.model.equipment.CommonEquipmentInfo
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.12.2020.
 */
@optics
data class TechnicalEquipment(
    @SerializedName("id")
    val id: String,
    @SerializedName("common")
    val commonEquipmentInfo: CommonEquipmentInfo,
    @SerializedName("power")
    val power: Float?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("type")
    val type: Reference?,
    @SerializedName("description")
    val description: String,
    @SerializedName("production")
    val production: String,
    @SerializedName("created")
    val created: Instant?
) {
    companion object {
        fun createEmpty(id: String): TechnicalEquipment {
            return TechnicalEquipment(
                id = id,
                commonEquipmentInfo = CommonEquipmentInfo.EMPTY,
                power = null,
                type = null,
                count = null,
                description = "",
                production = "",
                created = Instant.now()
            )
        }
    }
}