package ru.ktsstudio.feature_sync_impl.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startForegroundService
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import ru.ktsstudio.common.navigation.api.ModularNavigationApi
import ru.ktsstudio.common.ui.fragment.ViewBindingFragment
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.feature_sync_api.domain.model.SyncState
import ru.ktsstudio.feature_sync_impl.R
import ru.ktsstudio.feature_sync_impl.databinding.FragmentSyncBinding
import ru.ktsstudio.feature_sync_impl.di.DaggerSyncFeatureComponent
import ru.ktsstudio.feature_sync_impl.di.SyncFeatureComponent
import ru.ktsstudio.feature_sync_impl.presentation.SyncViewModel
import ru.ktsstudio.feature_sync_impl.service.SyncService
import javax.inject.Inject

class SyncFragment : ViewBindingFragment<FragmentSyncBinding>(FragmentSyncBinding::inflate) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: SyncViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(isFirstLaunch(savedInstanceState))

        viewModel.launchService.observe(this) {
            launchSyncService()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSkip.setOnClickListener {
            viewModel.finishSync()
        }
        binding.btnRetry.setOnClickListener {
            viewModel.onSyncClick()
        }

        viewModel.syncState.observe(viewLifecycleOwner, ::renderState)
    }

    private fun launchSyncService() {
        startForegroundService(requireContext(), Intent(requireContext(), SyncService::class.java))
    }

    private fun renderState(syncState: SyncState) {
        with(binding) {
            pbPrimary.isVisible = syncState != SyncState.ERROR
            viewError.isVisible = syncState == SyncState.ERROR
            btnRetry.isVisible = syncState == SyncState.ERROR
            btnSkip.isVisible = syncState == SyncState.ERROR

            when (syncState) {
                SyncState.STARTED -> {
                    tvTitle.setText(R.string.sync_title_in_progress)
                    tvBody.setText(R.string.sync_body_in_progress)
                }
                SyncState.ERROR -> {
                    tvTitle.setText(R.string.sync_title_error)
                    tvBody.setText(R.string.sync_body_error)
                }
                SyncState.FINISHED -> {
                    viewModel.finishSync()
                }
                SyncState.IDLE -> {}
            }.exhaustive
        }
    }

    private fun inject(isFirstLaunch: Boolean) {
        if (isFirstLaunch) {
            if(ComponentRegistry.hasComponent<SyncFeatureComponent>()) {
                ComponentRegistry.clear<SyncFeatureComponent>()
            }
            ComponentRegistry.register {
                DaggerSyncFeatureComponent.factory()
                    .create(
                        coreApi = ComponentRegistry.get(),
                        dependency = ComponentRegistry.get(),
                        syncNavigator = ComponentRegistry.get<ModularNavigationApi>().syncNavigator()
                    )
            }
        }

        ComponentRegistry.get<SyncFeatureComponent>()
            .fragmentComponent()
            .inject(this)
    }

    override fun onFullDestroy() {
        ComponentRegistry.clear<SyncFeatureComponent>()
    }
}