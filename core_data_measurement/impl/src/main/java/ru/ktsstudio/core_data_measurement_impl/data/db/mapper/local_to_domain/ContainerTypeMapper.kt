package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import javax.inject.Inject

class ContainerTypeMapper @Inject constructor() : Mapper<LocalContainerType, ContainerType> {
    override fun map(item: LocalContainerType): ContainerType {
        return ContainerType(id = item.id, name = item.name, isSeparate = item.isSeparate)
    }
}