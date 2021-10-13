package ru.ktsstudio.core_data_measurement_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainer
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 08.10.2020.
 */
class MnoContainersDbMapper @Inject constructor() : Mapper<Mno, List<@JvmSuppressWildcards LocalMnoContainer>> {
    override fun map(item: Mno): List<LocalMnoContainer> {
        val mnoId = item.objectInfo.mnoId
        return item.containers.map {
            LocalMnoContainer(
                id = it.id,
                mnoId = mnoId,
                name = it.name,
                typeId = it.type.id,
                volume = it.volume,
                scheduleType = it.scheduleType
            )
        }
    }
}