package ru.ktsstudio.feature_auth.presentation.auth_state

import ru.ktsstudio.common.ui.resource_manager.ResourceManager
import ru.ktsstudio.common.utils.isNetworkUnavailableException
import ru.ktsstudio.feature_auth.R
import ru.ktsstudio.feature_auth.domain.auth.AuthStatus
import javax.inject.Inject

/**
 * Created by Igor Park on 17/09/2020.
 */
internal class AuthStatusHandler @Inject constructor(
    private val resources: ResourceManager
) {
    fun handleAuthStatus(
        authStatus: AuthStatus,
        doOnComplete: () -> Unit,
        doOnFail: (errorMessage: String) -> Unit
    ) {
        when (authStatus) {
            is AuthStatus.Complete -> {
                doOnComplete.invoke()
            }
            is AuthStatus.Failed -> {
                val error: Throwable = authStatus.error
                val errorMessage = when {
                    error.isNetworkUnavailableException() -> {
                        resources.getString(
                            R.string.error_message_network_unavailable
                        )
                    }
                    error.localizedMessage != null -> error.localizedMessage
                    else -> resources.getString(R.string.error_unknown)
                }
                doOnFail.invoke(errorMessage)
            }
        }
    }
}
