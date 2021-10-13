package ru.ktsstudio.core_data_measurement_impl.data.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteMno

interface MnoNetworkApi {

    @GET("m/mno.list")
    fun getMnoList(): Single<List<RemoteMno>>
}