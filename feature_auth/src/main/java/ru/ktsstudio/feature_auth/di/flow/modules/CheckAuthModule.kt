package ru.ktsstudio.feature_auth.di.flow.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ktsstudio.common.ui.view_model.ViewModelKey
import ru.ktsstudio.feature_auth.presentation.auth_state.AuthStateViewModel

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
@Module
internal interface CheckAuthModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthStateViewModel::class)
    fun bindViewModel(impl: AuthStateViewModel): ViewModel

}