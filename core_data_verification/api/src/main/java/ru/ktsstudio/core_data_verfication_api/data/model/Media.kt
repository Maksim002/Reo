package ru.ktsstudio.core_data_verfication_api.data.model

import androidx.core.net.toUri
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint
import java.io.File

/**
 * @author Maxim Ovchinnikov on 25.11.2020.
 */
data class Media(
    @SerializedName("remoteId")
    val remoteId: String?,
    @SerializedName("remoteUrl")
    val remoteUrl: String?,
    @SerializedName("cachedFile")
    val cachedFile: File?,
    @SerializedName("gpsPoint")
    val gpsPoint: GpsPoint,
    @SerializedName("date")
    val date: Instant
) {
    val id = "$remoteUrl-${cachedFile?.toUri()}"
}
