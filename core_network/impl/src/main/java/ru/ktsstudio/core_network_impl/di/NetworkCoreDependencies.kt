package ru.ktsstudio.core_network_impl.di

import android.content.Context
import com.google.gson.Gson
import ru.ktsstudio.common.data.account.AccountManager
import ru.ktsstudio.common.data.AppVersion
import ru.ktsstudio.common.data.account.AuthDelegate
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_network_api.qualifiers.ApiUrl
import ru.ktsstudio.core_network_api.qualifiers.AuthApiUrl
import ru.ktsstudio.core_network_api.qualifiers.ClientId
import ru.ktsstudio.core_network_api.qualifiers.ClientSecret

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2019.
 */
interface NetworkCoreDependencies {
    @AppVersion
    fun appVersion(): String
    @ApiUrl
    fun apiUrl(): String
    @AuthApiUrl
    fun authApiUrl(): String
    @ClientId
    fun clientId(): String
    @ClientSecret
    fun clientSecret(): String
    fun context(): Context
    fun accountManager(): AccountManager
    fun authDelegate(): AuthDelegate
    fun schedulers(): SchedulerProvider
    fun gson(): Gson
}
