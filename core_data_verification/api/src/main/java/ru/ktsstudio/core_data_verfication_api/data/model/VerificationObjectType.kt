package ru.ktsstudio.core_data_verfication_api.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Ovchinnikov on 24.11.2020.
 */
enum class VerificationObjectType {
    @SerializedName("WASTE_TREATMENT")
    WASTE_TREATMENT,
    @SerializedName("WASTE_PLACEMENT")
    WASTE_PLACEMENT,
    @SerializedName("WASTE_RECYCLING")
    WASTE_RECYCLING,
    @SerializedName("WASTE_DISPOSAL")
    WASTE_DISPOSAL
}