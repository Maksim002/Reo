package ru.ktsstudio.feature_auth.presentation.auth_state

import androidx.lifecycle.LiveData
import ru.ktsstudio.common.data.auth.AuthRepository
import ru.ktsstudio.common.ui.view_model.RxViewModel
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.utilities.rx.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class AuthStateViewModel @Inject constructor(
    authRepository: AuthRepository,
    schedulers: SchedulerProvider
) : RxViewModel() {

    private val navigateLiveData = SingleLiveEvent<NavigationDirections>()

    val navigate: LiveData<NavigationDirections>
        get() = navigateLiveData

    init {
        authRepository.isLoggedIn()
            .observeOn(schedulers.ui)
            .map { if (it) NavigationDirections.MAIN else NavigationDirections.AUTH }
            .subscribe(navigateLiveData::postValue, Timber::e)
            .store()
    }

    enum class NavigationDirections {
        AUTH,
        MAIN
    }
}