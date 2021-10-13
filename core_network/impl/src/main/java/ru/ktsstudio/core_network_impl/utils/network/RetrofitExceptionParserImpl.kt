package ru.ktsstudio.core_network_impl.utils.network

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import ru.ktsstudio.common.data.network.RetrofitException
import ru.ktsstudio.core_network_api.RetrofitExceptionParser
import ru.ktsstudio.core_network_api.exceptions.BadRequestException
import ru.ktsstudio.core_network_api.exceptions.ForbiddenException
import ru.ktsstudio.core_network_api.exceptions.NotFoundException
import ru.ktsstudio.core_network_api.exceptions.UnauthorizedException
import timber.log.Timber
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2019.
 */
class RetrofitExceptionParserImpl @Inject constructor() : RetrofitExceptionParser {

    override fun fromRetrofitResponse(response: Response<*>): RetrofitException {
        val requestUrl = response
            .raw()
            .request()
            .url()
            .toString()

        val code = response.code()
        val rawAnswer = response.errorBody()?.string()

        return getException(requestUrl, code, rawAnswer)
    }

    override fun fromOkhttpResponse(response: okhttp3.Response): RetrofitException {
        val requestUrl = response
            .request()
            .url()
            .toString()

        val code = response.code()
        val rawAnswer = response.body()?.string()

        return getException(requestUrl, code, rawAnswer)
    }

    private fun getException(requestUrl: String, code: Int, rawAnswer: String?): RetrofitException {
        val serverErrorMessage: String = try {
            Gson().fromJson(
                rawAnswer,
                ApiErrorModel::class.java
            )?.fullError ?: PARSING_ERROR_MESSAGE_IS_NULL
        } catch (e: JsonSyntaxException) {
            Timber.e(e)
            PARSING_ERROR
        }

        val errorMessage = """
                |
                |Request URL: $requestUrl
                |
                |Error code: $code
                |
                |Server message: $serverErrorMessage
            """.trimMargin()

        return when (code) {
            HttpURLConnection.HTTP_NOT_FOUND -> NotFoundException(errorMessage)
            HttpURLConnection.HTTP_FORBIDDEN -> ForbiddenException(errorMessage)
            HttpURLConnection.HTTP_UNAUTHORIZED -> UnauthorizedException(errorMessage)
            HttpURLConnection.HTTP_BAD_REQUEST -> BadRequestException(errorMessage)
            else -> RetrofitException(errorMessage)
        }
    }

    companion object {
        private const val PARSING_ERROR = "Error while parsing server message"
        private const val PARSING_ERROR_MESSAGE_IS_NULL = "Error while parsing server message (message is null)"
    }
}
