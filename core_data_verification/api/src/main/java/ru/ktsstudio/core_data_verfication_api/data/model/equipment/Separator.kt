package ru.ktsstudio.core_data_verfication_api.data.model.equipment

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
@optics
data class Separator(
    @SerializedName("id")
    val id: String,
    @SerializedName("common")
    val commonEquipmentInfo: CommonEquipmentInfo,
    @SerializedName("type")
    val type: SeparatorType,
    @SerializedName("otherName")
    val otherName: String?,
    @SerializedName("created")
    val created: Instant?
) {
    companion object {
        fun createEmpty(id: String, type: SeparatorType): Separator {
            return Separator(
                id = id,
                commonEquipmentInfo = CommonEquipmentInfo.EMPTY,
                type = type,
                otherName = null,
                created = Instant.now()
            )
        }
    }
}