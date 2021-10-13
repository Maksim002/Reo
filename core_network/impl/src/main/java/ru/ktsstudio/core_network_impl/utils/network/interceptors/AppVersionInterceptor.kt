package ru.ktsstudio.core_network_impl.utils.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AppVersionInterceptor(
    private val appVersion: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("android_ver", appVersion)
            .build()

        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
