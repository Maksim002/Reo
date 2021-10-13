package ru.ktsstudio.core_data_verfication_api.data.model

import arrow.optics.optics

@optics
data class ProductionCheckedSurvey(
    val productCapacity: Boolean = false,
    val products: Boolean = false,
    val services: Boolean = false
) {
    companion object
}