package ru.ktsstudio.core_data_verfication_api.data.model.equipment

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
@optics
data class Press(
    @SerializedName("id")
    val id: String,
    @SerializedName("common")
    val commonEquipmentInfo: CommonEquipmentInfo,
    @SerializedName("type")
    val type: PressType,
    @SerializedName("otherName")
    val otherName: String?,
    @SerializedName("created")
    val created: Instant?
) {
    companion object {
        fun createEmpty(id: String, type: PressType): Press {
            return Press(
                id = id,
                commonEquipmentInfo = CommonEquipmentInfo.EMPTY,
                type = type,
                otherName = null,
                created = Instant.now()
            )
        }
    }
}