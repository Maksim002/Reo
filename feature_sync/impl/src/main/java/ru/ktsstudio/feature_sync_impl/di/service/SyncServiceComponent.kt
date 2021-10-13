package ru.ktsstudio.feature_sync_impl.di.service

import dagger.Subcomponent
import ru.ktsstudio.feature_sync_impl.service.SyncService

@Subcomponent
interface SyncServiceComponent {

    fun inject(service: SyncService)
}