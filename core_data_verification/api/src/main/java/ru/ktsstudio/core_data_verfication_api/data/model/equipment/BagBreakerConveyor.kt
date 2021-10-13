package ru.ktsstudio.core_data_verfication_api.data.model.equipment

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
@optics
data class BagBreakerConveyor(
    @SerializedName("id")
    val id: String,
    @SerializedName("common")
    val commonEquipmentInfo: CommonEquipmentInfo,
    @SerializedName("created")
    val created: Instant?
) {
    companion object {
        fun createEmpty(id: String): BagBreakerConveyor {
            return BagBreakerConveyor(
                id = id,
                commonEquipmentInfo = CommonEquipmentInfo.EMPTY,
                created = Instant.now()
            )
        }
    }
}