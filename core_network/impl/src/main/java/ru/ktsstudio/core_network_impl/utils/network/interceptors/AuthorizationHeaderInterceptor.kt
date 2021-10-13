package ru.ktsstudio.core_network_impl.utils.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.ktsstudio.common.data.account.AccountManager
import java.io.IOException
import kotlin.jvm.Throws

class AuthorizationHeaderInterceptor(
    private val accountManager: AccountManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .newBuilder()
            .header(
                AUTH_HEADER_NAME,
                accountManager.accessToken().blockingGet("").withBearer()
            )
            .build()
            .let { chain.proceed(it) }
    }

    companion object {
        const val AUTH_HEADER_NAME = "Authorization"
    }
}
