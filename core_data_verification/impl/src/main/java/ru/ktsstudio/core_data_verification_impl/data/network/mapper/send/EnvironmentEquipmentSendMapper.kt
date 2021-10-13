package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoringEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import javax.inject.Inject

class EnvironmentEquipmentSendMapper @Inject constructor(
    val referenceMapper: Mapper<Reference, RemoteReference>
) {
    fun map(
        items: List<EnvironmentMonitoringEquipment>
    ): List<RemoteInfrastructureObject> {
        return items.map { item ->
            with(item) {
                RemoteInfrastructureObject(
                    id = id,
                    type = systemTypeUsed?.let(referenceMapper::map)
                )
            }
        }
    }
}