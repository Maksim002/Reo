package ru.ktsstudio.app_verification.presentation.map

import ru.ktsstudio.common.data.models.GpsPoint

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */
data class ObjectInfoUiState(
    val isLoading: Boolean,
    val error: Throwable?,
    val info: List<Any>,
)

data class ObjectUiInfo(
    val id: String,
    val location: GpsPoint,
    val name: String,
    val address: String,
    val date: String,
    val status: String,
    val type: String
)
