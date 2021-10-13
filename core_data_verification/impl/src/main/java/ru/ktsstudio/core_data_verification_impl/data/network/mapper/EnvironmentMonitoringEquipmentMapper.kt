package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verfication_api.data.model.infrastructure.EnvironmentMonitoringEquipment
import ru.ktsstudio.core_data_verfication_api.data.model.reference.Reference
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteInfrastructureObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * Created by Igor Park on 11/01/2021.
 */
class EnvironmentMonitoringEquipmentMapper @Inject constructor(
    private val referenceMapper: Mapper<RemoteReference, Reference>
) : Mapper<RemoteInfrastructureObject, EnvironmentMonitoringEquipment> {
    override fun map(item: RemoteInfrastructureObject): EnvironmentMonitoringEquipment =
        with(item) {
            EnvironmentMonitoringEquipment(
                id = id.requireNotNull(),
                systemTypeUsed = type?.let(referenceMapper::map)
            )
        }
}