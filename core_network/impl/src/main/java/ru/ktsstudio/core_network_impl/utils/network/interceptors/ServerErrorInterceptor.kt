package ru.ktsstudio.core_network_impl.utils.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.ktsstudio.core_network_api.RetrofitExceptionParser
import java.net.HttpURLConnection.HTTP_BAD_REQUEST

class ServerErrorInterceptor(
    private val retrofitExceptionParcer: RetrofitExceptionParser
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code() >= HTTP_BAD_REQUEST) throw retrofitExceptionParcer.fromOkhttpResponse(
            response
        )
        return response
    }
}
