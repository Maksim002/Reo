package ru.ktsstudio.core_data_verification_impl.data.network

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ktsstudio.core_data_verfication_api.data.model.reference.RemoteReference
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteFile
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.network.model.send.EndSurveySendBody

/**
 * @author Maxim Ovchinnikov on 10.11.2020.
 */
interface RegisterNetworkApi {

    @GET("/api/references/all/")
    fun getReferences(): Single<List<RemoteReference>>

    @GET("/api/objects/mobile/")
    fun getVerificationObjectList(): Single<List<RemoteVerificationObject>>

    @PATCH("/api/objects/{objectId}")
    fun uploadObject(
        @Path("objectId") id: String,
        @Body verificationObject: RemoteVerificationObject
    ): Completable

    @POST("/api/survey/end")
    fun endSurvey(
        @Query("objectId") objectId: String,
    ): Completable
}