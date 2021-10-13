package ru.ktsstudio.core_data_verification_impl.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import retrofit2.Retrofit
import ru.ktsstudio.common.data.models.RemoteSettings
import ru.ktsstudio.common.data.models.Settings
import ru.ktsstudio.common.data.settings.SettingsStore
import ru.ktsstudio.common.di.qualifiers.Authority
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_network_api.qualifiers.ApiUrl
import ru.ktsstudio.core_network_api.qualifiers.AuthRetrofit
import ru.ktsstudio.core_network_api.qualifiers.DefaultRetrofit
import ru.ktsstudio.core_network_api.qualifiers.MediaRetrofit

interface DataCoreVerificationDependencies {

    fun context(): Context
    fun schedulers(): SchedulerProvider
    fun sharedPreferences(): SharedPreferences
    fun gson(): Gson
    @Authority
    fun authority(): String
    @ApiUrl
    fun apiUrl(): String

    @AuthRetrofit
    fun authRetrofit(): Retrofit
    @DefaultRetrofit
    fun defaultRetrofit(): Retrofit
    @MediaRetrofit
    fun mediaRetrofit(): Retrofit

    fun settingsStore(): SettingsStore
    fun settingsNetworkMapper(): Mapper<RemoteSettings, Settings>
}