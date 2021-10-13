package ru.ktsstudio.core_data_measurement_impl.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.data.models.Settings
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.core_data_measurement_impl.data.network.SettingsNetworkApi
import ru.ktsstudio.common.data.models.RemoteSettings
import ru.ktsstudio.common.data.settings.SettingsStore
import ru.ktsstudio.common.utils.rx.toRx3Observable
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
class SettingsRepositoryImpl @Inject constructor(
    private val settingsStore: SettingsStore,
    private val settingsNetworkApi: SettingsNetworkApi,
    private val settingsApiMapper: Mapper<RemoteSettings, Settings>,
    private val schedulerProvider: SchedulerProvider
) : SettingsRepository {

    override fun refreshSettings(): Completable {
        return settingsNetworkApi.getSettings()
            .flatMapCompletable(settingsStore::updateSettings)
            .subscribeOn(schedulerProvider.io)
    }

    override fun observeSettings(): Observable<Settings> {
        return settingsStore.observeSettings()
            .map(settingsApiMapper::map)
            .toRx3Observable()
            .distinctUntilChanged()
            .subscribeOn(schedulerProvider.io)
    }
}