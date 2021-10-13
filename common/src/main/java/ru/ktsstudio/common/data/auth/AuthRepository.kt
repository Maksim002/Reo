package ru.ktsstudio.common.data.auth

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
interface AuthRepository {
    fun isLoggedIn(): Single<Boolean>
    fun login(email: String, password: String): Completable
    fun logout(): Completable
}