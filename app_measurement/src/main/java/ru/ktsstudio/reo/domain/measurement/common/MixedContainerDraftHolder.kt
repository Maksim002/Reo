package ru.ktsstudio.reo.domain.measurement.common

import ru.ktsstudio.core_data_measurement_api.domain.MixedWasteContainerComposite

/**
 * Created by Igor Park on 22/11/2020.
 */
interface MixedContainerDraftHolder {
    fun getMixedContainerDraft(mixedContainerId: Long): MixedWasteContainerComposite?
    fun saveMixedContainerDraft(mixedContainer: MixedWasteContainerComposite)
}
