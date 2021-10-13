package ru.ktsstudio.reo.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import ru.ktsstudio.common.ui.BaseActivity
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.databinding.ActivityMainBinding
import ru.ktsstudio.reo.di.app.MeasurementAppComponent
import ru.ktsstudio.reo.navigation.Navigator
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var baseNavigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        ComponentRegistry.get<MeasurementAppComponent>().inject(this)
        baseNavigator.bind(findNavController(R.id.navHostFragment))
    }

    override fun onDestroy() {
        super.onDestroy()
        baseNavigator.unbind()
    }
}
