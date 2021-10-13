package ru.ktsstudio.common.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ru.ktsstudio.common.app.AppCodeProvider
import ru.ktsstudio.common.app.GooglePlayServicesChecker
import ru.ktsstudio.common.di.CoreApi
import ru.ktsstudio.common.ui.fragment.FirstLaunchChecker
import ru.ktsstudio.common_registry.ComponentRegistry

/**
 * @author Maxim Myalkin (MaxMyalkin) on 05.02.2021.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val appCodeProvider: AppCodeProvider = ComponentRegistry.get<CoreApi>().appCodeProvider()
    private val firstLaunchChecker = FirstLaunchChecker(appCodeProvider)

    override fun onCreate(savedInstanceState: Bundle?) {
        val isFirstLaunch = firstLaunchChecker.check(savedInstanceState)
        val savedState = if (isFirstLaunch) null else savedInstanceState
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        firstLaunchChecker.saveState(outState)
    }

    override fun onResume() {
        super.onResume()
        GooglePlayServicesChecker().check(this)
    }
}