package ru.ktsstudio.common.data.settings

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.common.data.models.Settings

/**
 * @author Maxim Ovchinnikov on 19.10.2020.
 */
interface SettingsRepository {

    fun refreshSettings(): Completable
    fun observeSettings(): Observable<Settings>
}