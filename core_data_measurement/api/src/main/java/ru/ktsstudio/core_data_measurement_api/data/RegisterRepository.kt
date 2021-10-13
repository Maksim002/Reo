package ru.ktsstudio.core_data_measurement_api.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_measurement_api.data.model.Register

/**
 * @author Maxim Ovchinnikov on 15.10.2020.
 */
interface RegisterRepository {

    fun fetchRegisters(): Single<Register>
}