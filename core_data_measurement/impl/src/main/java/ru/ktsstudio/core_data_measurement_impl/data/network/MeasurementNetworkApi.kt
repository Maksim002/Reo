package ru.ktsstudio.core_data_measurement_impl.data.network

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.ktsstudio.core_data_measurement_impl.data.network.model.MeasurementToSend
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteFile
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteMeasurement

interface MeasurementNetworkApi {

    @GET("m/measurement.list")
    fun getMeasurements(): Single<List<RemoteMeasurement>>

    @POST("m/measurement.create")
    fun sendMeasurement(
        @Body measurement: MeasurementToSend
    ): Maybe<RemoteMeasurement>
}