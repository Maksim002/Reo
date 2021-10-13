package ru.ktsstudio.core_data_verification_impl.data.db.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.FiasAddress
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteFiasAddress
import javax.inject.Inject

/**
 * Created by Igor Park on 04/12/2020.
 */
class FiasAddressToRemoteMapper @Inject constructor() : Mapper<FiasAddress, RemoteFiasAddress> {
    override fun map(item: FiasAddress): RemoteFiasAddress = with(item) {
        return RemoteFiasAddress(
            area = area,
            buildingNumber = buildingNumber,
            cadastralNumber = cadastralNumber,
            city = city,
            id = id,
            innerCityTerritory = innerCityTerritory,
            landPlotNumber = landPlotNumber,
            municipalRegionName = municipalRegionName,
            oktmo = oktmo,
            planningStructureElement = planningStructureElement,
            postalCode = postalCode,
            regionName = regionName,
            roomNumber = roomNumber,
            streetRoadElement = streetRoadElement
        )
    }
}