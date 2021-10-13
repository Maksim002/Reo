package ru.ktsstudio.feature_sync_impl.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import ru.ktsstudio.common.navigation.api.SyncNavigator
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.feature_sync_api.domain.SyncInteractor
import ru.ktsstudio.feature_sync_api.domain.model.SyncState
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import javax.inject.Inject

class SyncViewModel @Inject constructor(
    private val interactor: SyncInteractor,
    private val navigator: SyncNavigator
) : RxViewModel() {

    val syncState: LiveData<SyncState>
        get() = interactor.syncProgress.toLiveData()

    private val _launchService = SingleLiveEvent<Unit>()
    val launchService: LiveData<Unit> = _launchService

    init {
        onSyncClick()
    }

    fun onSyncClick() = syncInternal()

    fun finishSync() {
        interactor.reset()
        navigator.finishSync()
    }

    private fun syncInternal() {
        _launchService.value = Unit
    }
}