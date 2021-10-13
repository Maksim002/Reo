package ru.ktsstudio.core_data_measurement_impl.data.network

import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.ktsstudio.core_data_measurement_impl.data.network.model.RemoteFile

/**
 * Created by Igor Park on 11/11/2020.
 */
interface MediaApi {
    @Multipart
    @POST("api/files")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Single<RemoteFile>
}