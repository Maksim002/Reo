package ru.ktsstudio.feature_auth.di.flow

import dagger.Subcomponent
import ru.ktsstudio.feature_auth.di.flow.modules.CheckAuthModule
import ru.ktsstudio.feature_auth.ui.CheckAuthFragment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
@Subcomponent(modules = [CheckAuthModule::class])
internal interface CheckAuthComponent {
    fun inject(fragment: CheckAuthFragment)
}