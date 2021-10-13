package ru.ktsstudio.core_network_api

import retrofit2.Response
import ru.ktsstudio.common.data.network.RetrofitException

/**
 * @author Maxim Myalkin (MaxMyalkin) on 05.11.2019.
 */
interface RetrofitExceptionParser {

    fun fromRetrofitResponse(response: Response<*>): RetrofitException

    fun fromOkhttpResponse(response: okhttp3.Response): RetrofitException
}
