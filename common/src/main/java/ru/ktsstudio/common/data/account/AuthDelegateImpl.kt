package ru.ktsstudio.common.data.account

import android.os.Handler
import android.os.Looper
import ru.ktsstudio.common.navigation.api.ModularNavigationApi
import ru.ktsstudio.common_registry.ComponentRegistry
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class AuthDelegateImpl @Inject constructor() : AuthDelegate {
    override fun onUnauthorized() {
        Handler(Looper.getMainLooper()).post {
            ComponentRegistry.get<ModularNavigationApi>().authFailedNavigator().navigateToAuthScreen()
        }
    }
}