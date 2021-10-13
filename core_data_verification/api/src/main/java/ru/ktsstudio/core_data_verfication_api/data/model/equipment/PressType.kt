package ru.ktsstudio.core_data_verfication_api.data.model.equipment

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 09.12.2020.
 */
enum class PressType {
    @SerializedName("horizontal")
    HORIZONTAL,
    @SerializedName("vertical")
    VERTICAL,
    @SerializedName("compactor")
    COMPACTOR,
    @SerializedName("packingMachines")
    PACKING_MACHINES,
    @SerializedName("other")
    OTHER
}