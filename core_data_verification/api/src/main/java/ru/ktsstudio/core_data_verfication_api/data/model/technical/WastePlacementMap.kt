package ru.ktsstudio.core_data_verfication_api.data.model.technical

import arrow.optics.optics
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant

/**
 * @author Maxim Ovchinnikov on 18.12.2020.
 */
@optics
data class WastePlacementMap(
    @SerializedName("id")
    val id: String,
    @SerializedName("commissioningPeriod")
    val commissioningPeriod: Instant?,
    @SerializedName("area")
    val area: Float?
) {
    companion object {
        fun createEmpty(id: String): WastePlacementMap {
            return WastePlacementMap(
                id = id,
                commissioningPeriod = null,
                area = null
            )
        }
    }
}