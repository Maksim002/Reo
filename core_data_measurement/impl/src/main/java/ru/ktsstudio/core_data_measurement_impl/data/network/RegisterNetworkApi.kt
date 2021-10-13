package ru.ktsstudio.core_data_measurement_impl.data.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteRegister

/**
 * @author Maxim Ovchinnikov on 15.10.2020.
 */
interface RegisterNetworkApi {

    @GET("m/registers")
    fun getRegisterList(): Single<RemoteRegister>
}