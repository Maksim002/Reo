package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import javax.inject.Inject

class ContainerTypeDbMapper @Inject constructor() : Mapper<ContainerType, LocalContainerType> {

    override fun map(item: ContainerType): LocalContainerType {
        return LocalContainerType(id = item.id, name = item.name, isSeparate = item.isSeparate)
    }
}