package ru.ktsstudio.core_data_measurement_impl.data.network.mapper

import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.Category
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.core_data_measurement_api.data.model.ObjectInfo
import ru.ktsstudio.core_data_measurement_api.data.model.Source
import ru.ktsstudio.core_data_measurement_api.data.model.SourceAddress
import ru.ktsstudio.core_data_measurement_api.data.model.Unit
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteMno
import javax.inject.Inject

class MnoNetworkMapper @Inject constructor() : Mapper<RemoteMno, Mno> {

    override fun map(item: RemoteMno): Mno = with(item) {
        Mno(
            objectInfo = ObjectInfo(
                taskIds = taskIds.map { it.id },
                mnoId = id,
                gpsPoint = GpsPoint(latitude, longitude)
            ),
            containers = containers.map(::mapContainer),
            source = Source(
                name = sourceName,
                type = sourceType,
                category = Category(id = category.id, name = category.name),
                subcategory = subcategory,
                unit = Unit(
                    type = unitType,
                    quantity = unitQuantity
                ),
                altUnit = Unit(
                    type = altUnitType,
                    quantity = altUnitQuantity
                )
            ),
            sourceAddress = SourceAddress(
                federalDistrict = federalDistrict,
                region = region,
                municipalDistrict = municipalDistrict,
                address = address
            )
        )
    }

    private fun mapContainer(container: RemoteMno.Container) = with(container) {
        MnoContainer(
            id = id,
            name = name,
            type = ContainerType(
                id = type.id,
                name = type.name,
                isSeparate = type.isSeparate
            ),
            volume = volume,
            scheduleType = scheduleType.orEmpty()
        )
    }
}