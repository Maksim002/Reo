package ru.ktsstudio.reo.di.sync

import dagger.Binds
import dagger.Module
import ru.ktsstudio.common.di.AppScope
import ru.ktsstudio.reo.domain.sync.SyncInteractorImpl
import ru.ktsstudio.feature_sync_api.domain.SyncInteractor

/**
 * @author Maxim Ovchinnikov on 07.11.2020.
 */
@Module
interface SyncModule {

    @Binds
    @AppScope
    fun bindSyncInteractor(impl: SyncInteractorImpl): SyncInteractor
}
