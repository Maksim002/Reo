package ru.ktsstudio.reo.domain.measurement.common

import io.reactivex.rxjava3.core.Observable

/**
 * Created by Igor Park on 22/11/2020.
 */
interface MeasurementDraftHolder {
    fun observeMeasurementDraft(): Observable<MeasurementDraft>
    fun getSessionMeasurementDraft(): MeasurementDraft
    fun saveComment(comment: String)
    fun isEmpty(): Boolean
}
