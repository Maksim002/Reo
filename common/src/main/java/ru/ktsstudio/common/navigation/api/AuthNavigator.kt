package ru.ktsstudio.common.navigation.api

/**
 * Created by Igor Park on 30/09/2020.
 */
interface AuthNavigator {
    fun authFeatureCheckAuthToLogin()
    fun authFeatureCheckAuthToFinishAuth()
    fun authFeatureLoginToFinishAuth()
}