package ru.ktsstudio.app_verification.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.databinding.ActivityMainBinding
import ru.ktsstudio.app_verification.di.app.VerificationAppComponent
import ru.ktsstudio.app_verification.navigation.Navigator
import ru.ktsstudio.common.ui.BaseActivity
import ru.ktsstudio.common_registry.ComponentRegistry
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var baseNavigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        ComponentRegistry.get<VerificationAppComponent>().inject(this)
        baseNavigator.bind(findNavController(R.id.navHostFragment))
    }

    override fun onDestroy() {
        super.onDestroy()
        baseNavigator.unbind()
    }
}
