package ru.ktsstudio.core_data_verfication_api.data.model

import arrow.core.MapK
import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.production.Product
import ru.ktsstudio.core_data_verfication_api.data.model.production.Service

@optics
data class ProductionSurvey(
    val id: String,
    val productCapacity: Int?,
    val products: MapK<String, Product>,
    val services: MapK<String, Service>
) {
    companion object
}