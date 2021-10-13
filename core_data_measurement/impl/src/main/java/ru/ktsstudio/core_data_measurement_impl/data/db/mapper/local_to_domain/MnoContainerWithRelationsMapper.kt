package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainerWithRelations
import javax.inject.Inject

class MnoContainerWithRelationsMapper @Inject constructor(
    private val containerTypeMapper: Mapper<LocalContainerType, ContainerType>
) : Mapper<LocalMnoContainerWithRelations, MnoContainer> {
    override fun map(item: LocalMnoContainerWithRelations): MnoContainer = with(item.container) {
        return MnoContainer(
            id = id,
            name = name,
            volume = volume,
            type = containerTypeMapper.map(item.containerType),
            scheduleType = scheduleType
        )
    }
}