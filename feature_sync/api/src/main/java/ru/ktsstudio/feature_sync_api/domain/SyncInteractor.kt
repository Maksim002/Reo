package ru.ktsstudio.feature_sync_api.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import ru.ktsstudio.feature_sync_api.domain.model.SyncState

interface SyncInteractor {

    val syncProgress: Flowable<SyncState>

    fun sync(): Completable

    fun reset()
}