package ru.ktsstudio.core_data_verfication_api.data.model

import arrow.optics.optics
import com.google.gson.annotations.SerializedName

/**
 * Created by Igor Park on 14/12/2020.
 */
@optics
data class CommonMediaInfo(
    @SerializedName("photos")
    val photos: List<Media>,
    @SerializedName("passport")
    val passport: Media?
) {
    val isFilled: Boolean
        get() = photos.isNotEmpty() && passport != null

    companion object {
        val EMPTY = CommonMediaInfo(
            photos = emptyList(),
            passport = null
        )
    }
}