package ru.ktsstudio.core_data_measurement_impl.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import retrofit2.Retrofit
import ru.ktsstudio.common.di.qualifiers.Authority
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.data.models.RemoteSettings
import ru.ktsstudio.common.data.models.Settings
import ru.ktsstudio.common.data.settings.SettingsStore
import ru.ktsstudio.common.utils.id.IdGenerator
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_network_api.qualifiers.AuthRetrofit
import ru.ktsstudio.core_network_api.qualifiers.DefaultRetrofit
import ru.ktsstudio.core_network_api.qualifiers.MediaRetrofit

interface DataCoreMeasurementDependencies {

    fun context(): Context
    fun schedulers(): SchedulerProvider
    fun sharedPreferences(): SharedPreferences
    fun gson(): Gson
    fun resources(): ResourceManager
    @Authority
    fun authority(): String

    fun settingsStore(): SettingsStore
    fun settingsNetworkMapper(): Mapper<RemoteSettings, Settings>

    @AuthRetrofit
    fun authRetrofit(): Retrofit

    @MediaRetrofit
    fun mediaRetrofit(): Retrofit

    @DefaultRetrofit
    fun defaultRetrofit(): Retrofit
    
    fun idGenerator(): IdGenerator
}