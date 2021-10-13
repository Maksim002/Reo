package ru.ktsstudio.core_data_verification_impl.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.data.models.RemoteSettings
import ru.ktsstudio.common.data.models.Settings
import ru.ktsstudio.common.data.settings.SettingsRepository
import ru.ktsstudio.common.data.settings.SettingsStore
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx3Observable
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 11.11.2020.
 */
class SettingsRepositoryImpl @Inject constructor(
    private val settingsStore: SettingsStore,
    private val settingsApiMapper: Mapper<RemoteSettings, Settings>,
    private val schedulerProvider: SchedulerProvider
) : SettingsRepository {

    override fun refreshSettings(): Completable {
        return getSettings()
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

    private fun getSettings() = Single.just(
        RemoteSettings(
            supportEmail = SUPPORT_EMAIL,
            supportPhoneNumber = SUPPORT_PHONE
        )
    )

    companion object {
        private const val SUPPORT_EMAIL = "supportelfs@reo.ru"
        private const val SUPPORT_PHONE = "+7(495)139-70-77"
    }
}