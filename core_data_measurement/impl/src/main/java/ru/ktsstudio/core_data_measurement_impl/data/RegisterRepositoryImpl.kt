package ru.ktsstudio.core_data_measurement_impl.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.RegisterRepository
import ru.ktsstudio.core_data_measurement_api.data.model.ContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.Register
import ru.ktsstudio.core_data_measurement_api.data.model.WasteCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.ContainerStore
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_api.data.model.MeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.MeasurementStore
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory
import ru.ktsstudio.core_data_measurement_impl.data.network.RegisterNetworkApi
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteRegister
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 15.10.2020.
 */
class RegisterRepositoryImpl @Inject constructor(
    private val registerNetworkApi: RegisterNetworkApi,
    private val registerApiMapper: Mapper<RemoteRegister, Register>,
    private val schedulerProvider: SchedulerProvider
) : RegisterRepository {

    override fun fetchRegisters(): Single<Register> {
        return registerNetworkApi.getRegisterList()
            .map(registerApiMapper::map)
            .subscribeOn(schedulerProvider.io)
    }
}