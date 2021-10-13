package ru.ktsstudio.common.ui.fragment

import android.os.Bundle
import ru.ktsstudio.common.app.AppCodeProvider

/**
 * @author Maxim Myalkin (MaxMyalkin) on 05.02.2021.
 */
class FirstLaunchChecker(
    private val appCodeProvider: AppCodeProvider
) {

    fun saveState(outState: Bundle) {
        outState.putString(KEY_APP_HASH_CODE, appCodeProvider.appCode)
    }

    fun check(savedInstanceState: Bundle?): Boolean {
        val savedAppHashCode = savedInstanceState?.getString(KEY_APP_HASH_CODE) ?: ""
        return savedAppHashCode != appCodeProvider.appCode
    }

    companion object {
        private const val KEY_APP_HASH_CODE = "KEY_APP_HASH_CODE"
    }
}