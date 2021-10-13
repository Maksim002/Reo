package ru.ktsstudio.app_verification.data.map

import ru.ktsstudio.common.domain.models.MapObject

/**
 * Created by Igor Park on 04/10/2020.
 */
data class VerificationMapObject(
    override val id: String,
    override val latitude: Double,
    override val longitude: Double
) : MapObject
