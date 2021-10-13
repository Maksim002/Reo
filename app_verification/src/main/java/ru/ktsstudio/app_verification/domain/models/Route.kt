package ru.ktsstudio.app_verification.domain.models

import ru.ktsstudio.common.data.models.GpsPoint

/**
 * @author Maxim Ovchinnikov on 14.11.2020.
 */
data class Route(
    val start: GpsPoint,
    val destination: GpsPoint
)
