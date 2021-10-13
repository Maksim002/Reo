package ru.ktsstudio.common.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import ru.ktsstudio.common.app.ActivityProvider
import ru.ktsstudio.common.app.AppCodeProvider
import ru.ktsstudio.common.data.AppVersion
import ru.ktsstudio.common.data.account.AccountManager
import ru.ktsstudio.common.data.account.AuthDelegate
import ru.ktsstudio.common.data.error.ErrorReporter
import ru.ktsstudio.common.data.location.LocationProvider
import ru.ktsstudio.common.data.location.LocationRepository
import ru.ktsstudio.common.data.models.RemoteSettings
import ru.ktsstudio.common.data.models.Settings
import ru.ktsstudio.common.data.network.state.NetworkStateProvider
import ru.ktsstudio.common.data.settings.SettingsStore
import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.id.IdGenerator
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.09.2020.
 */
interface CoreApi {
    fun appCodeProvider(): AppCodeProvider
    fun activityProvider(): ActivityProvider
    fun locationProvider(): LocationProvider
    fun networkStateProvider(): NetworkStateProvider
    fun activityLifecycleCallbacks(): Application.ActivityLifecycleCallbacks
    fun context(): Context
    fun errorReporter(): ErrorReporter

    @AppVersion
    fun appVersion(): String
    fun accountManager(): AccountManager
    fun authDelegate(): AuthDelegate
    fun sharedPreferences(): SharedPreferences
    fun schedulers(): SchedulerProvider
    fun resourceManager(): ResourceManager
    fun gson(): Gson

    fun settingsStore(): SettingsStore
    fun settingsNetworkMapper(): Mapper<RemoteSettings, Settings>

    fun locationRepository(): LocationRepository
    fun idGenerator(): IdGenerator
}