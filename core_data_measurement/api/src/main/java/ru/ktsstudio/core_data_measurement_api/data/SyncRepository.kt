package ru.ktsstudio.core_data_measurement_api.data

import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.core_data_measurement_api.data.model.Measurement
import ru.ktsstudio.core_data_measurement_api.data.model.Mno
import ru.ktsstudio.core_data_measurement_api.data.model.Register

/**
 * @author Maxim Myalkin (MaxMyalkin) on 18.11.2020.
 */
interface SyncRepository {
    fun saveSyncData(
        registers: Register,
        mnoList: List<Mno>,
        measurementList: List<Measurement>
    ): Completable
}