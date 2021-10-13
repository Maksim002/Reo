package ru.ktsstudio.feature_map.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.launch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_recycle_map.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions
import ru.ktsstudio.feature_map.data.MapResourceProvider
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.sync.SyncHost
import ru.ktsstudio.common.utils.keyboard.setKeyboardListener
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.setStroke
import ru.ktsstudio.common.utils.setValueWithoutEventTrigger
import ru.ktsstudio.common.utils.showRationaleDialog
import ru.ktsstudio.common.utils.updateDrawableLayer
import ru.ktsstudio.common.utils.view.AfterTextChangedWatcher
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.feature_map.R
import ru.ktsstudio.feature_map.di.RecycleMapFeatureComponent
import ru.ktsstudio.feature_map.domain.models.Destination
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.ui.OpenSettingsContract
import ru.ktsstudio.feature_map.presentation.MapViewModel
import ru.ktsstudio.feature_map.presentation.RecycleObject
import ru.ktsstudio.utilities.extensions.consume
import ru.ktsstudio.utilities.extensions.requireNotNull
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject

/**
 * Created by Igor Park on 29/09/2020.
 */
@RuntimePermissions
class MapFragment : BaseFragment(R.layout.fragment_recycle_map) {

    @Inject
    lateinit var resourceProvider: MapResourceProvider

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: MapViewModel by viewModels { factory }
    private var clusterManager: ClusterManager<RecycleObject>? = null
    private var map: GoogleMap? = null
    private var rationaleDialog: AlertDialog by autoCleared()
    private val mapHost: MapHost
        get() = parentFragment as MapHost
    private val syncHost: SyncHost
        get() = parentFragment as SyncHost
    private val isMapViewInflated: Boolean
        get() = mapView != null
    private var searchInput: EditText by autoCleared()
    private var filterButton: ImageView by autoCleared()
    private var filterSetIndicator: View by autoCleared()
    private val searchTextListener = AfterTextChangedWatcher {
        viewModel.updateSearchQuery(it)
    }
    private val openLocationSettingsCall = registerForActivityResult(
        OpenSettingsContract(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    ) { viewModel.showMyLocation() }

    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentRegistry.get<RecycleMapFeatureComponent>().inject(this)
        super.onCreate(savedInstanceState)
        setKeyboardListener { isVisible ->
            if (isVisible.not()) {
                searchInput.clearFocus()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            map = googleMap
            if (isMapViewInflated) onMapReady(googleMap)
        }

        initToolbar()
        initFilterView()
        bindViewModel()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // mapView is null when fragment is in backstack
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        map = null
        clusterManager = null
    }

    override fun onFullDestroy() {
        ComponentRegistry.clear<RecycleMapFeatureComponent>()
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
    fun showMyLocation() {
        viewModel.checkGpsState()
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
        rationaleDialog = requireContext().showRationaleDialog(
            dialogMessage = R.string.dialog_location_permission_title,
            request = request
        )
    }

    private fun initToolbar() {
        requireView().findViewById<TextView>(R.id.multilineTitle).text = resourceProvider.getMapTitle()
        with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
            inflateMenu(R.menu.sync_menu)
            menu.findItem(R.id.action_refresh).actionView.setOnClickListener {
                syncHost.refresh()
            }
        }
    }

    private fun initFilterView() {
        searchInput = requireView().findViewById(R.id.searchInput)
        searchInput.hint = resourceProvider.getMapSearchHint()
        filterButton = requireView().findViewById(R.id.filterImageView)
        filterButton.updateDrawableLayer(R.id.strokeLayer) {
            it.setStroke(
                strokeWidth = resources.getDimensionPixelSize(R.dimen.strokeWidth),
                color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
        }
        filterSetIndicator = requireView().findViewById(R.id.filterSetIndicator)
    }

    private fun onMapReady(googleMap: GoogleMap) {
        clusterManager = ClusterManager(context, googleMap)
        val notNullClusterManager = clusterManager.requireNotNull()
        notNullClusterManager.renderer =
            RecycleMarkerRenderer(requireContext(), notNullClusterManager, googleMap)
        restoreCameraState()
        initSettings()
        initListeners(notNullClusterManager)
        viewModel.mapReady()
    }

    private fun initSettings() {
        val map = map ?: return
        with(map.uiSettings) {
            isCompassEnabled = false
            isMapToolbarEnabled = false
            isMyLocationButtonEnabled = true
            isZoomControlsEnabled = false
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = false
            isIndoorLevelPickerEnabled = false
            isMyLocationButtonEnabled = false
        }
        map.setOnMarkerClickListener(clusterManager)
    }

    private fun restoreCameraState() {
        viewModel.animateCamera.value
            ?.let { center ->
                centerMap(center, withAnimation = false)
            }
    }

    private fun bindViewModel() = with(viewModel) {
        searchInput.addTextChangedListener(searchTextListener)
        filterButton.setOnClickListener { openFilter() }
        viewLifecycleOwner.observeNotNull(mapState, ::applyState)
        viewLifecycleOwner.observeNotNull(errorMessage, ::toast)
        viewLifecycleOwner.observeNotNull(animateCamera, ::centerMap)
        viewLifecycleOwner.observeNotNull(recycleObjects, ::showRecycleMarkers)
        viewLifecycleOwner.observeNotNull(navigate, ::openObjectInfo)
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<String>(INFO_DIALOG_ON_FULL_DESTROY_KEY)?.observe(viewLifecycleOwner) { _ ->
                clearObjectInfo()
            }
        viewLifecycleOwner.observeNotNull(gpsEnableRequest) {
            mapHost.openGeoDataRequestDialog()
        }
        mapHost.observeGpsRequestDialogResults(this@MapFragment) { gpsRequestResult ->
            if (gpsRequestResult) {
                openLocationSettingsCall.launch()
            } else {
                toast(resources.getString(R.string.geo_data_request_reject_toast_my_location))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun applyState(state: MapUiState) {
        map?.isMyLocationEnabled = state.isMyLocationEnabled
        showMe.isEnabled = state.isMyLocationLoading.not()
        myLocationLoading.isVisible = state.isMyLocationLoading
        myLocationReady.isVisible = state.isMyLocationLoading.not()
        filterSetIndicator.isVisible = state.isFilterSet
        searchInput.setValueWithoutEventTrigger(state.searchQuery, searchTextListener)
    }

    private fun openObjectInfo(objectIds: List<String>) {
        if (objectIds.isNotEmpty()) mapHost.openObjectsInfo(objectIds)
    }

    private fun centerMap(destination: Destination, withAnimation: Boolean = true) {
        val newLatLng = destination.center.gpsPoint.toLatLng()
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, destination.center.zoom)
        if (withAnimation) {
            map?.animateCamera(cameraUpdate, MAP_CENTER_ANIMATION_DURATION, object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    if (destination.showObjectInfo) viewModel.showSelectedObjects()
                }

                override fun onCancel() {}
            })
        } else {
            map?.moveCamera(cameraUpdate)
            if (destination.showObjectInfo) viewModel.showSelectedObjects()
        }
    }

    private fun showRecycleMarkers(objects: List<RecycleObject>) {
        val notNullClusterManager = clusterManager ?: return
        notNullClusterManager.clearItems()
        objects.forEach(notNullClusterManager::addItem)
        notNullClusterManager.cluster()
    }

    private fun initListeners(clusterManager: ClusterManager<RecycleObject>) {
        map?.apply {
            setOnCameraIdleListener {
                val visibleRegion = projection.visibleRegion
                val topLeft = visibleRegion.farLeft.toGpsPoint()
                val bottomRight = visibleRegion.nearRight.toGpsPoint()
                val center = visibleRegion.latLngBounds.center.toGpsPoint()

                viewModel.setVisibleRegion(
                    topLeft = topLeft,
                    bottomRight = bottomRight,
                    center = center,
                    zoom = cameraPosition.zoom
                )
            }
        }
        showMe.setOnClickListener { showMyLocationWithPermissionCheck() }

        clusterManager.setOnClusterItemClickListener {
            consume { viewModel.selectObject(it.mapObject) }
        }
        clusterManager.setOnClusterClickListener {
            consume { viewModel.selectObjects(it.items.map { it.mapObject }, it.position.toGpsPoint()) }
        }
    }

    private fun GpsPoint.toLatLng(): LatLng {
        return LatLng(lat, lng)
    }

    private fun LatLng.toGpsPoint(): GpsPoint {
        return GpsPoint(
            lat = latitude,
            lng = longitude
        )
    }

    companion object {
        fun getInstance(): MapFragment = MapFragment()

        private const val MAP_CENTER_ANIMATION_DURATION = 500
        const val INFO_DIALOG_ON_FULL_DESTROY_KEY = "info_dialog_on_full_destroy_key"
    }
}