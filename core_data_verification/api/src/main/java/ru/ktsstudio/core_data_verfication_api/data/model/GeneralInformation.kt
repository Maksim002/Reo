package ru.ktsstudio.core_data_verfication_api.data.model

import arrow.optics.optics
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference

/**
 * Created by Igor Park on 04/12/2020.
 */
@optics
data class GeneralInformation(
    val name: String,
    val subject: Reference,
    val fiasAddress: FiasAddress?,
    val addressDescription: String?
) {
    companion object
}

data class FiasAddress(
    val area: String?,
    val buildingNumber: String?,
    val cadastralNumber: String?,
    val city: String?,
    val id: String,
    val innerCityTerritory: String?,
    val landPlotNumber: String?,
    val municipalRegionName: String?,
    val oktmo: String?,
    val planningStructureElement: String?,
    val postalCode: String?,
    val regionName: String?,
    val roomNumber: String?,
    val streetRoadElement: String?
) {

    val fullAddress: String
        get() = listOf(
            municipalRegionName,
            city,
            streetRoadElement,
            buildingNumber,
            roomNumber
        ).mapNotNull {
            it?.takeIf { it.isNotBlank() }
        }.joinToString()
}