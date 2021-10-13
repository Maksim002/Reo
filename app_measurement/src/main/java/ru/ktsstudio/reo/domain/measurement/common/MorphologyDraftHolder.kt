package ru.ktsstudio.reo.domain.measurement.common

import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.core_data_measurement_api.domain.MorphologyItem

/**
 * Created by Igor Park on 22/11/2020.
 */
interface MorphologyDraftHolder {
    fun observeMorphologyDraft(): Observable<Set<MorphologyItem>>
    fun getMorphologyItemDraft(categoryId: Long): MorphologyItem?
    fun saveMorphologyItem(item: MorphologyItem)
}
