package ru.ktsstudio.reo.domain.measurement.common

import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerWasteType
import ru.ktsstudio.core_data_measurement_api.domain.SeparateContainerComposite

/**
 * Created by Igor Park on 22/11/2020.
 */
interface SeparateContainerDraftHolder {
    fun saveSeparateContainerDraft(container: SeparateContainerComposite)
    fun getSeparateContainerDraft(containerId: Long): SeparateContainerComposite?
    fun isContainerAddedToMeasurement(containerId: Long): Boolean
    fun deleteSeparateContainerDraft(containerId: Long)

    fun observeContainerWasteTypeDrafts(containerId: Long): Observable<List<ContainerWasteType>>
    fun initWasteTypeList(containerToWasteTypesMap: Map<Long, Set<ContainerWasteType>>)
    fun clearContainerWasteTypeDrafts(containerId: Long)
}
