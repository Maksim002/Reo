package ru.ktsstudio.feature_auth.domain.auth

/**
 * Created by Igor Park on 01/04/2020.
 */
internal sealed class AuthStatus {
    object Complete : AuthStatus()
    data class Failed(val error: Throwable) : AuthStatus()
}
