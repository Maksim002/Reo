package ru.ktsstudio.common.di

import ru.ktsstudio.common.data.auth.AuthRepository

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.09.2020.
 */
interface CoreAuthApi {
    fun authRepository(): AuthRepository
}