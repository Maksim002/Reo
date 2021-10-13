package ru.ktsstudio.core_data_verfication_api.data.model.equipment

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
enum class SeparatorType {
    @SerializedName("metal")
    METAL,
    @SerializedName("iron")
    IRON,
    @SerializedName("optic")
    OPTIC,
    @SerializedName("eddy_current")
    EDDY_CURRENT,
    @SerializedName("ballistic")
    BALLISTIC,
    @SerializedName("fraction")
    FRACTION,
    @SerializedName("vibration")
    VIBRATION,
    @SerializedName("plastic")
    PLASTIC,
    @SerializedName("other")
    OTHER
}