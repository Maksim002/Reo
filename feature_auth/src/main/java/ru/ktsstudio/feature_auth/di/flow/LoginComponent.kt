package ru.ktsstudio.feature_auth.di.flow

import dagger.Subcomponent
import ru.ktsstudio.feature_auth.di.flow.modules.CheckFormModule
import ru.ktsstudio.feature_auth.di.flow.modules.LoginModule
import ru.ktsstudio.feature_auth.ui.LoginFragment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
@Subcomponent(modules = [LoginModule::class, CheckFormModule::class])
internal interface LoginComponent {
    fun inject(fragment: LoginFragment)
}