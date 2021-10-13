package ru.ktsstudio.feature_map.domain.models

import ru.ktsstudio.common.data.models.GpsPoint

/**
 * Created by Igor Park on 02/10/2020.
 */
internal data class CameraPosition(
    val topLeft: GpsPoint,
    val bottomRight: GpsPoint,
    val center: Center
)