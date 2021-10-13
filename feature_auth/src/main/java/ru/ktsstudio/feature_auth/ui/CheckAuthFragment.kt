package ru.ktsstudio.feature_auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.feature_auth.di.flow.AuthFlowComponent
import ru.ktsstudio.common.navigation.api.AuthNavigator
import ru.ktsstudio.feature_auth.presentation.auth_state.AuthStateViewModel
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 24.09.2020.
 */
internal class CheckAuthFragment : BaseFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: AuthNavigator

    private val viewModel: AuthStateViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthFlowComponent.create().checkAuthComponent().inject(this)
        observeNotNull(viewModel.navigate, {
            when (it) {
                AuthStateViewModel.NavigationDirections.AUTH -> {
                    navigator.authFeatureCheckAuthToLogin()
                }
                AuthStateViewModel.NavigationDirections.MAIN -> {
                    navigator.authFeatureCheckAuthToFinishAuth()
                }
            }
        })
    }
}