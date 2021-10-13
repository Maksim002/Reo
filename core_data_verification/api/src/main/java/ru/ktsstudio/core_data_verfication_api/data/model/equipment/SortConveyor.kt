package ru.ktsstudio.core_data_verfication_api.data.model.equipment

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
@optics
data class SortConveyor(
    @SerializedName("id")
    val id: String,
    @SerializedName("common")
    val commonEquipmentInfo: CommonEquipmentInfo,
    @SerializedName("conveyor_info")
    val commonConveyorInfo: CommonConveyorInfo,
    @SerializedName("wasteHeight")
    val wasteHeight: Float?,
    @SerializedName("sortPointCount")
    val sortPointCount: Int?,
    @SerializedName("created")
    val created: Instant?
) {
    companion object {
        fun createEmpty(id: String): SortConveyor {
            return SortConveyor(
                id = id,
                commonEquipmentInfo = CommonEquipmentInfo.EMPTY,
                commonConveyorInfo = CommonConveyorInfo.EMPTY,
                wasteHeight = null,
                sortPointCount = null,
                created = Instant.now()
            )
        }
    }
}
