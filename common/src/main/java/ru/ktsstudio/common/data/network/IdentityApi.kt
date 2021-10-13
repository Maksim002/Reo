package ru.ktsstudio.common.data.network

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import ru.ktsstudio.common.data.models.RemoteToken
import ru.ktsstudio.common.data.models.RemoteUser

/**
 * @author Maxim Myalkin (MaxMyalkin) on 25.09.2020.
 */
interface IdentityApi {

    @POST("/connect/token")
    @FormUrlEncoded
    fun login(
        @Field("grant_type", encoded = true) grantType: String,
        @Field("username", encoded = true) login: String,
        @Field("password", encoded = true) password: String,
        @Field("client_id", encoded = true) clientId: String,
        @Field("client_secret", encoded = true) clientSecret: String
    ): Single<RemoteToken>

    @POST("/connect/revocation")
    @FormUrlEncoded
    fun logout(
        @Field("token", encoded = true) token: String,
        @Field("client_id", encoded = true) clientId: String,
        @Field("client_secret", encoded = true) clientSecret: String
    ): Completable

    @GET("/connect/userinfo")
    fun getUserInfo(): Single<RemoteUser>
}