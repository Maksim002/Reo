package ru.ktsstudio.app_verification.domain.models

import android.net.Uri
import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint
import java.io.File

/**
 * Created by Igor Park on 17/10/2020.
 */
data class CapturingMedia(
    val outputUri: Uri,
    val cachedFile: File,
    val gpsPoint: GpsPoint,
    val date: Instant
)
