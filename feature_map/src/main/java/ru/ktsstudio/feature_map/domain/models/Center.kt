package ru.ktsstudio.feature_map.domain.models

import ru.ktsstudio.common.data.models.GpsPoint

internal data class Center(
    val gpsPoint: GpsPoint,
    val zoom: Float
)