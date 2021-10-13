package ru.ktsstudio.feature_sync_api.di

import ru.ktsstudio.feature_sync_api.domain.SyncInteractor

/**
 * @author Maxim Ovchinnikov on 07.11.2020.
 */
interface CoreSyncDependency {
    fun syncInteractor(): SyncInteractor
}