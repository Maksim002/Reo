package ru.ktsstudio.core_data_measurement_impl.data.network.mapper

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus
import ru.ktsstudio.core_data_measurement_api.data.model.Register
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategory
import ru.ktsstudio.core_data_measurement_api.data.model.WasteGroup
import ru.ktsstudio.core_data_measurement_api.data.model.WasteSubgroup
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteRegister
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 15.10.2020.
 */
class RegisterNetworkMapper @Inject constructor() : Mapper<RemoteRegister, Register> {

    override fun map(item: RemoteRegister): Register = with(item) {
        Register(
            containerTypes = containerTypes.map { containerType ->
                ContainerType(
                    id = containerType.id,
                    name = containerType.name,
                    isSeparate = containerType.isSeparate
                )
            },
            wasteCategories = wasteCategories.map { category ->
                WasteCategory(
                    id = category.id,
                    name = category.name
                )
            },
            measurementStatuses = measurementStatuses.map { measurementStatus ->
                MeasurementStatus(
                    id = measurementStatus.id,
                    name = measurementStatus.name,
                    order = measurementStatus.order
                )
            },
            wasteGroups = wasteGroups.map { wasteGroup ->
                WasteGroup(
                    id = wasteGroup.id,
                    name = wasteGroup.name
                )
            },
            wasteSubgroups = wasteSubgroups.map { wasteSubgroup ->
                WasteSubgroup(
                    id = wasteSubgroup.id,
                    groupId = wasteSubgroup.groupId,
                    name = wasteSubgroup.name
                )
            }
        )
    }
}