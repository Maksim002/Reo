package ru.ktsstudio.core_data_verfication_api.data.model.equipment

import arrow.optics.optics
import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
@optics
data class CommonConveyorInfo(
    @SerializedName("length")
    val length: Float?,
    @SerializedName("width")
    val width: Float?,
    @SerializedName("speed")
    val speed: Float?
) {
    companion object {
        val EMPTY = CommonConveyorInfo(
            length = null,
            width = null,
            speed = null,
        )
    }
}