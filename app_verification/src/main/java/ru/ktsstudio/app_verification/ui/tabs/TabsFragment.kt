package ru.ktsstudio.app_verification.ui.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_tabs.*
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.di.app.VerificationAppComponent
import ru.ktsstudio.app_verification.di.tabs.TabsComponent
import ru.ktsstudio.app_verification.presentation.tabs.TabsViewModel
import ru.ktsstudio.app_verification.ui.object_list.ObjectListFragment
import ru.ktsstudio.common.ui.dialog.GpsRequestDialogDelegate
import ru.ktsstudio.common.ui.dialog.GpsRequestDialogFragment
import ru.ktsstudio.common.ui.dialog.ConfirmDialogFragment
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.sync.SyncHost
import ru.ktsstudio.common.utils.navigateSafe
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_verfication_api.di.CoreVerificationDataApi
import ru.ktsstudio.feature_map.starter.RecycleMapFeatureStarter
import ru.ktsstudio.feature_map.ui.MapHost
import ru.ktsstudio.settings.starter.SettingsFeatureStarter
import ru.ktsstudio.utilities.extensions.consume
import ru.ktsstudio.utilities.extensions.hideKeyboard
import ru.ktsstudio.utilities.extensions.requireNotNull
import javax.inject.Inject

/**
 * Created by Igor Park on 30/09/2020.
 */
class TabsFragment : BaseFragment(R.layout.fragment_tabs), MapHost, SyncHost {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var activeScreen: Tab

    private val viewModel: TabsViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            activeScreen = savedInstanceState.get(ACTIVE_TAB_KEY) as Tab
        } else {
            activeScreen = Tab.OBJECTS_MAP
            initFragments()
        }
        TabsComponent.create().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigationView()
        bindViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(ACTIVE_TAB_KEY, activeScreen)
    }

    override fun openObjectsInfo(objectIds: List<String>) {
        findNavController().navigateSafe(
            TabsFragmentDirections.actionTabsFragmentToObjectInfoDialogFragment(objectIds.toTypedArray())
        )
    }

    override fun openGeoDataRequestDialog() {
        val bundle = Bundle().apply {
            putString(
                GpsRequestDialogFragment.MESSAGE_ARG_KEY,
                resources.getString(R.string.geo_data_request_dialog_message_my_location)
            )
        }
        findNavController().navigate(R.id.gpsRequestDialogFragment, bundle)
    }

    override fun observeGpsRequestDialogResults(
        childFragment: Fragment,
        result: (Boolean) -> Unit
    ) {
        val gpsRequestDialogDelegate = GpsRequestDialogDelegate(findNavController(), viewLifecycleOwner)
        gpsRequestDialogDelegate.observeGpsRequestDialogResults(result)
    }

    override fun refresh() {
        viewModel.refreshData()
    }

    private fun initFragments() {
        fun FragmentTransaction.addAndHideFragment(
            tag: String,
            fragment: Fragment
        ) = add(R.id.tabHostFragment, fragment, tag).hide(fragment)

        val fragmentMap = hashMapOf(
            Tab.OBJECTS_MAP to getMapFeatureFragment(),
            Tab.OBJECTS_LIST to ObjectListFragment(),
            Tab.SETTINGS to getSettingsFeatureFragment()
        )

        childFragmentManager.beginTransaction().apply {
            fragmentMap.map {
                addAndHideFragment(
                    tag = it.key.name,
                    fragment = it.value
                )
            }
        }
            .show(fragmentMap.getValue(activeScreen))
            .commit()
    }

    private fun getMapFeatureFragment(): Fragment {
        return RecycleMapFeatureStarter.start(
            coreApi = ComponentRegistry.get(),
            coreNetworkApi = ComponentRegistry.get(),
            dependency = ComponentRegistry.get<VerificationAppComponent>().mapDependencyComponent()
        )
    }

    private fun getSettingsFeatureFragment(): Fragment {
        return SettingsFeatureStarter.start(
            coreApi = ComponentRegistry.get(),
            coreAuthApi = ComponentRegistry.get(),
            settingsNavigationApi = ComponentRegistry.get(),
            settingsRepository = ComponentRegistry.get<CoreVerificationDataApi>().settingsRepository()
        )
    }

    private fun setupBottomNavigationView() {

        fun showFragment(screen: Tab) = consume {
            childFragmentManager.beginTransaction()
                .hide(childFragmentManager.findFragmentByTag(activeScreen.name).requireNotNull())
                .show(childFragmentManager.findFragmentByTag(screen.name).requireNotNull())
                .commit()
            activeScreen = screen
        }

        bottomNavigation.apply {
            setOnNavigationItemSelectedListener { item ->
                requireActivity().hideKeyboard()
                when (item.itemId) {
                    R.id.item_mno_map -> showFragment(Tab.OBJECTS_MAP)
                    R.id.item_mno_list -> showFragment(Tab.OBJECTS_LIST)
                    R.id.item_settings -> showFragment(Tab.SETTINGS)
                    else -> false
                }
            }
        }
    }

    private fun bindViewModel() = with(viewModel) {
        viewLifecycleOwner.observeNotNull(sync, { navigateToSync() })
        viewLifecycleOwner.observeNotNull(networkUnavailable, { showOfflineDialog() })
    }

    private fun navigateToSync() {
        findNavController().navigateSafe(TabsFragmentDirections.actionTabsFragmentToSyncFragment())
    }

    private fun showOfflineDialog() {
        ConfirmDialogFragment.getInstance(
            title = getString(R.string.offline_dialog_title),
            message = getString(R.string.offline_dialog_message),
            positive = getString(android.R.string.ok),
            tag = OFFLINE_DIALOG_TAG
        ).show(parentFragmentManager, OFFLINE_DIALOG_TAG)
    }

    companion object {
        const val ACTIVE_TAB_KEY = "active_tab_key"
        const val OFFLINE_DIALOG_TAG = "offline_dialog_tag"
    }
}
