package ru.ktsstudio.core_network_impl.utils.network.interceptors

import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.ktsstudio.common.data.account.AccountManager
import ru.ktsstudio.common.data.account.AuthDelegate
import ru.ktsstudio.common.data.models.RefreshTokenModel
import ru.ktsstudio.utilities.extensions.consume
import ru.ktsstudio.utilities.extensions.orFalse
import ru.ktsstudio.utilities.extensions.requireNotNull
import timber.log.Timber
import java.net.HttpURLConnection
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Suppress("MagicNumber")
class AuthorizationFailedInterceptor(
    private val accountManager: AccountManager,
    private val authDelegate: AuthDelegate,
    private val authApiUrl: String,
    private val clientId: String,
    private val clientSecret: String,
    private val gson: Gson
) : Interceptor {

    /**
     * https://stackoverflow.com/a/33862068
     */
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequestTimestamp = System.currentTimeMillis()

        val originalResponse = chain.proceed(chain.request())

        if (originalResponse.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

            val latch = getLatch()

            return when {
                latch != null && latch.count > 0 -> handleTokenIsUpdating(
                    chain,
                    originalResponse,
                    latch
                )
                tokenUpdateTime > originalRequestTimestamp -> updateTokenAndProceedChain(
                    chain,
                    originalResponse.request()
                )
                else -> handleTokenNeedRefresh(chain, originalResponse)
            }
        }

        return originalResponse
    }

    private fun handleTokenIsUpdating(
        chain: Interceptor.Chain,
        originalResponse: Response,
        latch: CountDownLatch
    ): Response {
        return if (latch.await(REQUEST_TIMEOUT, TimeUnit.SECONDS)) {
            updateTokenAndProceedChain(chain, originalResponse.request())
        } else {
            originalResponse
        }
    }

    private fun handleTokenNeedRefresh(
        chain: Interceptor.Chain,
        originalResponse: Response
    ): Response {
        return if (refreshToken(chain, originalResponse.request())) {
            updateTokenAndProceedChain(chain, originalResponse.request())
        } else {
            originalResponse
        }
    }

    private fun updateTokenAndProceedChain(
        chain: Interceptor.Chain,
        originRequest: Request
    ): Response {
        val newRequest = updateOriginalCallWithNewToken(originRequest)
        return chain.proceed(newRequest)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun refreshToken(chain: Interceptor.Chain, originalRequest: Request): Boolean {
        initLatch()

        val refreshCredentials = accountManager.refreshToken().blockingGet("")
        accountManager.updateAccessToken(null).blockingAwait()


        val refreshBody = FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("refresh_token", refreshCredentials)
            .add("client_id", clientId)
            .add("client_secret", clientSecret)
            .build()

        val refreshTokenRequest = originalRequest
            .newBuilder()
            .removeHeader("Authorization")
            .url(HttpUrl.parse("$authApiUrl/$REFRESH_TOKEN_PATH").requireNotNull())
            .post(refreshBody)
            .build()

        return try {
            val refreshTokenResponse = chain.proceed(refreshTokenRequest)
            when (refreshTokenResponse.code()) {
                HttpURLConnection.HTTP_OK -> refreshTokenResponse.body()
                    ?.string()
                    ?.let { gson.fromJson(it, RefreshTokenModel::class.java) }
                    ?.also {
                        accountManager.updateTokens(
                            accessToken = it.accessToken,
                            refreshToken = it.refreshToken
                        ).blockingAwait()
                        tokenUpdateTime = System.currentTimeMillis()
                        getLatch()?.countDown()
                    }
                    ?.let { true }
                    .orFalse()
                HttpURLConnection.HTTP_UNAUTHORIZED,
                HttpURLConnection.HTTP_BAD_REQUEST -> {
                    authDelegate.onUnauthorized()
                    false
                }
                else -> false
            }
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            authDelegate.onUnauthorized()
            false
        }
    }

    private fun updateOriginalCallWithNewToken(request: Request): Request {
        val newCredentials = accountManager.accessToken().blockingGet("")
        return request
            .newBuilder()
            .header(AuthorizationHeaderInterceptor.AUTH_HEADER_NAME, newCredentials.withBearer())
            .build()
    }

    companion object {
        private const val REQUEST_TIMEOUT = 30L
        private const val REFRESH_TOKEN_PATH = "connect/token"

        @Volatile
        private var tokenUpdateTime: Long = 0L

        private var countDownLatch: CountDownLatch? = null

        @Synchronized
        fun initLatch() {
            countDownLatch = CountDownLatch(1)
        }

        @Synchronized
        fun getLatch() = countDownLatch
    }
}
