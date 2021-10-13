package ru.ktsstudio.app_verification.ui.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.dialog_object_info.view.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.di.map.object_info.ObjectInfoComponent
import ru.ktsstudio.app_verification.presentation.map.ObjectInfoUiState
import ru.ktsstudio.app_verification.presentation.map.ObjectInfoViewModel
import ru.ktsstudio.app_verification.ui.map.adapter.ObjectInfoAdapter
import ru.ktsstudio.app_verification.domain.models.Route
import ru.ktsstudio.app_verification.ui.map.navigation_app_intents.DublgisMapIntentDelegate
import ru.ktsstudio.app_verification.ui.map.navigation_app_intents.GoogleMapIntentDelegate
import ru.ktsstudio.app_verification.ui.map.navigation_app_intents.MapsMeMapIntentDelegate
import ru.ktsstudio.app_verification.ui.map.navigation_app_intents.YandexMapIntentDelegate
import ru.ktsstudio.common.ui.dialog.GpsRequestDialogDelegate
import ru.ktsstudio.common.ui.dialog.GpsRequestDialogFragment
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.ui.OpenSettingsContract
import ru.ktsstudio.common.ui.bottom_sheet.BottomSheetDialogFragmentWithRoundedCorners
import ru.ktsstudio.common.utils.checkAvailableReceiver
import ru.ktsstudio.common.utils.navigateSafe
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.showRationaleDialog
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.feature_map.ui.MapFragment
import ru.ktsstudio.utilities.extensions.orFalse
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 13.11.2020.
 */

@RuntimePermissions
class ObjectInfoDialogFragment : BottomSheetDialogFragmentWithRoundedCorners<FrameLayout>(
    R.layout.dialog_object_info
) {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: ObjectInfoViewModel by viewModels { factory }

    private val args: ObjectInfoDialogFragmentArgs by navArgs()

    private val openLocationSettingsCall = registerForActivityResult(
        OpenSettingsContract(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    ) { viewModel.openMapWithRoute() }

    private var objectInfoAdapter: ObjectInfoAdapter by autoCleared()
    private var rationaleDialog: AlertDialog by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectInfoComponent.create(args.objectIds.toList()).inject(this)
    }

    override fun initView(contentView: View) {
        initViewModel()
        initList(contentView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    override fun onFullDestroy() {
        super.onFullDestroy()
        findNavController().currentBackStackEntry?.savedStateHandle?.set(
            MapFragment.INFO_DIALOG_ON_FULL_DESTROY_KEY,
            ""
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun openMapWithRoute(destination: GpsPoint) {
        viewModel.checkGpsState(destination)
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
        rationaleDialog = requireContext().showRationaleDialog(
            dialogMessage = R.string.dialog_location_permission_title,
            request = request
        )
    }

    private fun initViewModel() = with(viewModel) {
        observeNotNull(state, ::renderState)
        observeNotNull(navigateRouteMap, ::navigateToRouteMap)
        observeNotNull(toastMessage, ::toast)
        observeNotNull(gpsEnableRequest) {
            val bundle = Bundle().apply {
                putString(
                    GpsRequestDialogFragment.MESSAGE_ARG_KEY,
                    resources.getString(R.string.geo_data_request_dialog_message_route)
                )
            }
            parentFragment?.findNavController()?.navigate(R.id.gpsRequestDialogFragment, bundle)
        }
        observeGpsRequestDialogResult()
    }

    private fun observeGpsRequestDialogResult() {
        GpsRequestDialogDelegate(findNavController(), this)
            .observeGpsRequestDialogResults { gpsRequestResultApproved ->
                if (gpsRequestResultApproved) {
                    openLocationSettingsCall.launch()
                } else {
                    toast(resources.getString(R.string.geo_data_request_reject_toast_route))
                }
            }
    }

    private fun initList(contentView: View) {

        fun openObjectInspection(objectId: String) {
            findNavController().navigateSafe(
                ObjectInfoDialogFragmentDirections.actionObjectInfoToObjectInspection(objectId)
            )
        }

        objectInfoAdapter = ObjectInfoAdapter(
            viewModel::retry,
            ::openObjectInspection,
            ::openMapWithRouteWithPermissionCheck
        )
        contentView.objectInfoList.adapter = objectInfoAdapter
    }

    private fun renderState(state: ObjectInfoUiState) {
        containerView?.slider?.isVisible = state.isLoading.not()
        objectInfoAdapter.setDataWithState(
            data = state.info,
            isNextPageError = false,
            isNextPageLoading = false,
            isPrevPageError = false,
            isPrevPageLoading = false,
            isLoading = state.isLoading,
            error = state.error
        )
    }

    private fun navigateToRouteMap(route: Route) {

        fun openGoogleMapBrowser() {
            val routeFormatted = "${route.destination.lat},${route.destination.lng}"
            val googleMapUri = "https://www.google.com/maps/dir/?api=1&destination=$routeFormatted".toUri()

            val intent = Intent(Intent.ACTION_VIEW, googleMapUri)
            val browserInstalled = activity?.packageManager?.let {
                intent.checkAvailableReceiver(it) != null
            }.orFalse()

            if (browserInstalled) {
                activity?.startActivity(intent)
            } else {
                toast(getString(R.string.browser_is_not_available))
            }
        }

        val mapOpenedInApp = listOf(
            GoogleMapIntentDelegate(route),
            YandexMapIntentDelegate(route),
            DublgisMapIntentDelegate(route),
            MapsMeMapIntentDelegate(route)
        ).any {
            it.openMap(activity as Activity)
        }

        if (mapOpenedInApp.not()) openGoogleMapBrowser()
    }
}
