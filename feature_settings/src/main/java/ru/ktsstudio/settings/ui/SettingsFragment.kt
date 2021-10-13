package ru.ktsstudio.settings.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_settings.*
import ru.ktsstudio.common.navigation.intent.ImplicitIntent
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.sync.SyncHost
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.common.data.models.Settings
import ru.ktsstudio.feature_settings.R
import ru.ktsstudio.settings.di.SettingsComponent
import ru.ktsstudio.settings.domain.SettingsFeature
import ru.ktsstudio.settings.presentation.SettingsViewModel
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 18.10.2020.
 */
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: SettingsViewModel by viewModels { factory }

    private val syncHost: SyncHost
        get() = parentFragment as SyncHost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentRegistry.get<SettingsComponent>().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    override fun onFullDestroy() {
        super.onFullDestroy()
        ComponentRegistry.clear<SettingsComponent>()
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        setTitle(R.string.settings_title)
        inflateMenu(R.menu.sync_menu)
        menu.findItem(R.id.action_refresh).actionView.setOnClickListener {
            syncHost.refresh()
        }
    }

    private fun bindViewModel() = with(viewModel) {
        viewLifecycleOwner.observeNotNull(state, ::renderState)
        viewLifecycleOwner.observeNotNull(navigate, ::navigate)
        viewLifecycleOwner.observeNotNull(errorMessage, { toast(it) })
        supportEmailView.setOnClickListener {
            sendSupportEmail()
        }
        supportPhoneView.setOnClickListener {
            callSupportPhoneNumber()
        }
        logoutButton.setOnClickListener {
            logout()
        }
        requireView().findViewById<View>(R.id.retryButton).setOnClickListener {
            retry()
        }
    }

    private fun renderState(state: SettingsFeature.State) = with(state) {

        fun showLoading() {
            settingsLoading.isVisible = true
            settingsErrorView.isVisible = false
            supportGroup.isVisible = false
        }

        fun showErrorView(error: Throwable) {
            settingsLoading.isVisible = false
            settingsErrorView.isVisible = true
            supportGroup.isVisible = false

            requireView().findViewById<TextView>(R.id.title).text =
                getString(R.string.default_server_error_title)
            requireView().findViewById<TextView>(R.id.message).text =
                error.localizedMessage?.let {
                    getString(R.string.error_message_server_error, it)
                } ?: getString(R.string.error_page_message)
        }

        fun showSettings(settings: Settings) {
            settingsLoading.isVisible = false
            settingsErrorView.isVisible = false
            supportGroup.isVisible = true

            supportEmailGroup.isVisible = settings.supportEmail != null
            supportEmailValue.text = settings.supportEmail

            supportCallGroup.isVisible = settings.supportPhoneNumber != null
            supportCallValue.text = settings.supportPhoneNumber
        }

        when {
            loading -> showLoading()
            error != null -> showErrorView(error)
            else -> showSettings(settings)
        }
    }

    private fun navigate(implicitIntent: ImplicitIntent) {
        startActivity(implicitIntent.getIntent())
    }
}