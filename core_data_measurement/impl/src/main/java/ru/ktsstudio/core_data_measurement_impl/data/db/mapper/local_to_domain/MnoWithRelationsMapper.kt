package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.Category
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.core_data_measurement_api.data.model.ObjectInfo
import ru.ktsstudio.core_data_measurement_api.data.model.Source
import ru.ktsstudio.core_data_measurement_api.data.model.SourceAddress
import ru.ktsstudio.core_data_measurement_api.data.model.Unit
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoWithRelations
import javax.inject.Inject

class MnoWithRelationsMapper @Inject constructor() : Mapper<LocalMnoWithRelations, Mno> {

    override fun map(item: LocalMnoWithRelations): Mno = with(item) {
        Mno(
            objectInfo = ObjectInfo(
                taskIds = mno.taskIds,
                mnoId = mno.id,
                gpsPoint = mno.location
            ),
            source = Source(
                name = mno.nameSource,
                type = mno.typeSource,
                category = Category(category.id, category.name),
                subcategory = mno.subcategory,
                unit = Unit(
                    type = mno.typeUnit,
                    quantity = mno.quantityUnit
                ),
                altUnit = Unit(
                    type = mno.typeUnitAlt,
                    quantity = mno.quantityUnitAlt
                )
            ),
            sourceAddress = SourceAddress(
                federalDistrict = mno.federalDistrict,
                region = mno.region,
                municipalDistrict = mno.municipalDistrict,
                address = mno.address
            ),
            containers = containers.map {
                val container = it.container
                val containerType = it.containerType
                MnoContainer(
                    id = container.id,
                    name = container.name,
                    type = ContainerType(
                        id = containerType.id,
                        name = containerType.name,
                        isSeparate = containerType.isSeparate
                    ),
                    volume = container.volume,
                    scheduleType = container.scheduleType
                )
            }
        )
    }
}