package ru.ktsstudio.core_network_api

import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.ktsstudio.core_network_api.qualifiers.ApiUrl
import ru.ktsstudio.core_network_api.qualifiers.AuthApiUrl
import ru.ktsstudio.core_network_api.qualifiers.MediaRetrofit
import ru.ktsstudio.core_network_api.qualifiers.AuthOkhttpClient
import ru.ktsstudio.core_network_api.qualifiers.AuthRetrofit
import ru.ktsstudio.core_network_api.qualifiers.ClientId
import ru.ktsstudio.core_network_api.qualifiers.ClientSecret
import ru.ktsstudio.core_network_api.qualifiers.DefaultRetrofit
import ru.ktsstudio.core_network_api.qualifiers.IdentityRetrofit
import ru.ktsstudio.core_network_api.qualifiers.MediaOkhttpClient

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.09.2020.
 */
interface CoreNetworkApi {
    @MediaRetrofit
    fun mediaRetrofit(): Retrofit

    @DefaultRetrofit
    fun defaultRetrofit(): Retrofit

    @AuthRetrofit
    fun authRetrofit(): Retrofit

    @IdentityRetrofit
    fun identityRetrofit(): Retrofit

    @AuthOkhttpClient
    fun authOkHttpClient(): OkHttpClient

    @MediaOkhttpClient
    fun mediaOkHttpClient(): OkHttpClient

    fun flipperNetworkPlugin(): NetworkFlipperPlugin

    fun retrofitExceptionParser(): RetrofitExceptionParser

    @ApiUrl
    fun apiUrl(): String

    @AuthApiUrl
    fun authApiUrl(): String

    @ClientId
    fun authClientId(): String

    @ClientSecret
    fun clientSecret(): String
}