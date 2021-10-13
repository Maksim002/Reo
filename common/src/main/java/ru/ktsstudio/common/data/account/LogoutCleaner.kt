package ru.ktsstudio.common.data.account

import io.reactivex.rxjava3.core.Completable

/**
 * @author Maxim Myalkin (MaxMyalkin) on 16.11.2020.
 */
interface LogoutCleaner {
    fun cleanOnLogout(): Completable
}