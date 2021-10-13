package ru.ktsstudio.reo.domain.measurement.common

import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType

/**
 * Created by Igor Park on 22/11/2020.
 */
interface WasteTypeDraftHolder {
    fun getContainerWasteTypeDraft(wasteTypeId: String): ContainerWasteType?
    fun saveContainerWasteTypeDraft(containerId: Long, wasteType: ContainerWasteType)
    fun deleteContainerWasteTypeDraft(containerId: Long, wasteTypeId: String)
}
