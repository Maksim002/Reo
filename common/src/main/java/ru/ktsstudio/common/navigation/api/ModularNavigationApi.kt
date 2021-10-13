package ru.ktsstudio.common.navigation.api

/**
 * Created by Igor Park on 01/10/2020.
 */
interface ModularNavigationApi {
    fun authNavigator(): AuthNavigator
    fun recycleMapNavigator(): RecycleMapNavigator
    fun syncNavigator(): SyncNavigator
    fun authFailedNavigator(): AuthFailedNavigator
}