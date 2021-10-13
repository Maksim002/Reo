package ru.ktsstudio.common.data.account

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.09.2020.
 */
interface AccountManager {
    fun accessToken(): Maybe<String>
    fun refreshToken(): Maybe<String>
    fun updateTokens(
        accessToken: String?,
        refreshToken: String?
    ): Completable
    fun updateAccessToken(accessToken: String?): Completable
}