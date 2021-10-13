package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.local_to_domain

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MnoContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainerWithRelations
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 15.10.2020.
 */
class MnoContainerMapper @Inject constructor() :
    Mapper<LocalMnoContainerWithRelations, MnoContainer> {
    override fun map(item: LocalMnoContainerWithRelations): MnoContainer = with(item.container) {
        val containerType = item.containerType
        MnoContainer(
            id = id,
            name = name,
            volume = volume,
            type = ContainerType(
                id = containerType.id,
                name = containerType.name,
                isSeparate = containerType.isSeparate
            ),
            scheduleType = scheduleType
        )
    }
}