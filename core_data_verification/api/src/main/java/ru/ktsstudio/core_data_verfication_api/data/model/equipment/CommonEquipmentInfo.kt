package ru.ktsstudio.core_data_verfication_api.data.model.equipment

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import ru.ktsstudio.core_data_verfication_api.data.model.CommonMediaInfo

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
@optics
data class CommonEquipmentInfo(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("manufacturer")
    val manufacturer: String,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("mediaInfo")
    val commonMediaInfo: CommonMediaInfo
) {
    val isFilled: Boolean
        get() {
            return brand.isNotBlank() &&
                manufacturer.isNotBlank() &&
                count != null &&
                commonMediaInfo.isFilled
        }

    companion object {
        val EMPTY = CommonEquipmentInfo(
            brand = "",
            manufacturer = "",
            count = null,
            commonMediaInfo = CommonMediaInfo.EMPTY
        )
    }
}