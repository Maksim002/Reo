package ru.ktsstudio.app_verification.navigation

import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.ui.tabs.TabsFragmentDirections
import ru.ktsstudio.common.navigation.BaseNavigator
import ru.ktsstudio.common.navigation.api.AuthFailedNavigator
import ru.ktsstudio.common.navigation.api.AuthNavigator
import ru.ktsstudio.common.navigation.api.RecycleMapNavigator
import ru.ktsstudio.common.navigation.api.SyncNavigator
import ru.ktsstudio.common.utils.navigateSafe
import ru.ktsstudio.common.utils.resetNavGraph
import ru.ktsstudio.feature_auth.ui.CheckAuthFragmentDirections
import ru.ktsstudio.feature_auth.ui.LoginFragmentDirections
import ru.ktsstudio.feature_sync_impl.ui.SyncFragmentDirections
import ru.ktsstudio.settings.navigation.SettingsNavigator
import javax.inject.Inject

class Navigator @Inject constructor() : BaseNavigator(),
    AuthNavigator,
    RecycleMapNavigator,
    ObjectNavigator,
    SyncNavigator,
    SettingsNavigator,
    AuthFailedNavigator {

    override fun authFeatureCheckAuthToLogin() {
        navController?.navigateSafe(CheckAuthFragmentDirections.actionCheckAuthToLogin())
    }

    override fun authFeatureCheckAuthToFinishAuth() {
        navController?.navigateSafe(CheckAuthFragmentDirections.actionCheckAuthToSyncFragment())
    }

    override fun authFeatureLoginToFinishAuth() {
        navController?.navigateSafe(LoginFragmentDirections.actionLoginToSync())
    }

    override fun openObjectInspection(objectId: String) {
        navController?.navigateSafe(TabsFragmentDirections.actionTabsToObjectInspection(objectId))
    }

    override fun openObjectFilter() {
        navController?.navigateSafe(TabsFragmentDirections.actionTabsFragmentToObjectFilter())
    }

    override fun mapFeatureToObjectFilter() {
        navController?.navigateSafe(TabsFragmentDirections.actionTabsFragmentToObjectFilter())
    }

    override fun finishSync() {
        navController?.navigateSafe(SyncFragmentDirections.actionSyncFragmentToTabsFragment())
    }

    override fun settingsLogout() {
        navController?.resetNavGraph(R.navigation.nav_graph)
    }

    override fun navigateToAuthScreen() {
        navController?.resetNavGraph(R.navigation.nav_graph)
    }
}
