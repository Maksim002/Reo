package ru.ktsstudio.core_data_verification_impl.data.network

import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteFile

/**
 * @author Maxim Myalkin (MaxMyalkin) on 05.02.2021.
 */
interface FileNetworkApi {

    @Multipart
    @POST("/api/file/upload")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Single<RemoteFile>

}