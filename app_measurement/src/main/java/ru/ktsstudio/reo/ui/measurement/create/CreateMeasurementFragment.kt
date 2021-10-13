package ru.ktsstudio.reo.ui.measurement.create

import android.Manifest
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.PermissionChecker
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_create_measurement.*
import permissions.dispatcher.PermissionRequest
import ru.ktsstudio.common.ui.adapter.delegates.CardBottomCornersAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardDividerAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardEmptyLineAdapterDelegate
import ru.ktsstudio.common.ui.adapter.delegates.CardTitleItemWitAccentAdapterDelegate
import ru.ktsstudio.common.ui.fragment.BaseFragment
import ru.ktsstudio.common.ui.view.MarginInternalItemDecoration
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.showRationaleDialog
import ru.ktsstudio.common.utils.view.HideElevationScrollListener
import ru.ktsstudio.common.utils.view.autoCleared
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_data_measurement_api.domain.MeasurementMediaCategory
import ru.ktsstudio.reo.R
import ru.ktsstudio.reo.di.app.MeasurementAppComponent
import ru.ktsstudio.reo.di.create_measurement.EditMeasurementComponent
import ru.ktsstudio.reo.domain.measurement.create_measurement.CapturingMedia
import ru.ktsstudio.reo.presentation.measurement.create_measurement.CreateMeasurementUiState
import ru.ktsstudio.reo.presentation.measurement.create_measurement.CreateMeasurementViewModel
import ru.ktsstudio.common.ui.permissions.MultiplePermissionsContract
import ru.ktsstudio.common.ui.OpenSettingsContract
import ru.ktsstudio.common.ui.dialog.GpsRequestDialogDelegate
import ru.ktsstudio.reo.ui.common.TakeVideoContract
import ru.ktsstudio.reo.ui.measurement.create.adapters.CreateMeasurementAdapter
import ru.ktsstudio.reo.ui.measurement.create.adapters.MorphologyCardAdapterDelegate
import ru.ktsstudio.utilities.extensions.hideKeyboard
import ru.ktsstudio.utilities.extensions.toast
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by Igor Park on 14/10/2020.
 */
class CreateMeasurementFragment : BaseFragment(R.layout.fragment_create_measurement) {

    private val args: CreateMeasurementFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var rationaleDialog: AlertDialog by autoCleared()
    private var selectedMediaCategory: MeasurementMediaCategory? = null

    override val backPressedHandler: (() -> Unit)?
        get() = ::exitWithSaveMeasurementCheck

    private var createMeasurementAdapter: CreateMeasurementAdapter by autoCleared()

    private val captureMediaPermissionCall = prepareMediaPermissionCallback(
        permissions = PERMISSIONS_FOR_MEDIA,
        requiresPermission = { gpsRequiredAction = GpsRequiredAction.CAPTURE_MEDIA }
    )

    private val captureDocumentsPermissionCall = prepareMediaPermissionCallback(
        permissions = PERMISSIONS_FOR_DOCUMENTS,
        requiresPermission = { gpsRequiredAction = GpsRequiredAction.CAPTURE_MEDIA }
    )

    private val createMeasurementPermissionCall = prepareMediaPermissionCallback(
        permissions = PERMISSIONS_LOCATION,
        requiresPermission = { gpsRequiredAction = GpsRequiredAction.CREATE_MEASUREMENT }
    )

    private var gpsRequiredAction: GpsRequiredAction by Delegates.observable(
        GpsRequiredAction.COMPLETED
    ) { _, _, newValue ->
        if (newValue != GpsRequiredAction.COMPLETED) viewModel.checkGpsState()
    }

    private val capturePhotoCall = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success -> if (success) viewModel.mediaCaptured() }

    private val captureVideoCall = registerForActivityResult(
        TakeVideoContract(
            quality = LOW_QUALITY,
            duration = FIVE_MINUTES
        )
    ) { success -> if (success) viewModel.mediaCaptured() }

    private val openLocationSettingsCall = registerForActivityResult(
        OpenSettingsContract(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    ) { proceedGpsRequiredAction(isGpsEnabled = true) }

    private val viewModel: CreateMeasurementViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(isFirstLaunch(savedInstanceState))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initLists()
        initListeners()
        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDetach()
    }

    override fun onFullDestroy() {
        ComponentRegistry.clear<EditMeasurementComponent>()
    }

    private fun inject(isFirstLaunch: Boolean) {
        if (isFirstLaunch) {
            if (ComponentRegistry.hasComponent<EditMeasurementComponent>()) {
                ComponentRegistry.clear<EditMeasurementComponent>()
            }
            ComponentRegistry.register {
                ComponentRegistry.get<MeasurementAppComponent>()
                    .editMeasurementComponentFactory()
                    .create(mnoId = args.mnoId)
            }
        }
        ComponentRegistry.get<EditMeasurementComponent>()
            .createMeasurementComponentFactory()
            .create(args.measurementLocalId?.toLong())
            .inject(this)
    }

    private fun initToolbar() = with(requireView().findViewById<Toolbar>(R.id.toolbar)) {
        val isEditMode = args.measurementLocalId != null
        val title = if (isEditMode) {
            R.string.measurement_edit_measure
        } else {
            R.string.measurement_new_card_measure
        }
        setTitle(title)
        setNavigationIcon(R.drawable.ic_arrow_left)
        setNavigationOnClickListener { exitWithSaveMeasurementCheck() }
    }

    private fun initLists() {
        createMeasurementAdapter = CreateMeasurementAdapter(
            onRetry = viewModel::initMeasurement,
            onAddEntity = viewModel::addEntity,
            onContainerClick = viewModel::openContainer,
            onDeleteMedia = viewModel::deleteMedia,
            onAddMedia = ::addMedia,
            onTextChanged = viewModel::onCommentChanged
        )
        with(createMeasurementList) {
            adapter = createMeasurementAdapter
            addItemDecoration(getSpaceItemDecoration())
            addOnScrollListener(
                HideElevationScrollListener(
                    bottomBlock,
                    resources.getDimensionPixelSize(R.dimen.default_elevation)
                )
            )
            setHasFixedSize(true)
        }
    }

    private fun initListeners() {
        backToStartMeasurement.setOnClickListener {
            exitWithSaveMeasurementCheck()
        }
        navigateToPreview.setOnClickListener {
            requireActivity().hideKeyboard()
            createMeasurementPermissionCall.launch()
        }
    }

    private fun addMedia(category: MeasurementMediaCategory) {
        selectedMediaCategory = category
        when (category) {
            MeasurementMediaCategory.ACT_PHOTO -> captureDocumentsPermissionCall.launch()
            MeasurementMediaCategory.PHOTO,
            MeasurementMediaCategory.VIDEO -> captureMediaPermissionCall.launch()
        }
    }

    private fun proceedGpsRequiredAction(isGpsEnabled: Boolean) {
        when (gpsRequiredAction) {
            GpsRequiredAction.CAPTURE_MEDIA -> {
                selectedMediaCategory?.let { category ->
                    viewModel.captureMediaRequest(
                        category,
                        withLocation = isGpsEnabled
                    )
                }
            }
            GpsRequiredAction.CREATE_MEASUREMENT -> {
                viewModel.setMeasurementLocation()
            }
            GpsRequiredAction.COMPLETED -> Unit
        }.exhaustive
        gpsRequiredAction = GpsRequiredAction.COMPLETED
    }

    private fun showRationale(allPermissions: Array<String>, request: PermissionRequest) {
        val dialogMessage = StringBuilder().apply {
            appendLine(getString(R.string.dialog_permission_title))
            allPermissions.forEach { permission ->
                val rationaleMessageRes = mapPermissionToRationale(permission)
                if (checkPermissionGranted(permission).not() && rationaleMessageRes != null) {
                    appendLine(getString(rationaleMessageRes))
                }
            }
        }
            .toString()
        rationaleDialog = requireContext().showRationaleDialog(
            dialogMessage = dialogMessage,
            request = request
        )
    }

    private fun bindViewModel() {
        with(viewModel) {
            viewLifecycleOwner.observeNotNull(errorMessage, { toast(it.message, it.length) })
            viewLifecycleOwner.observeNotNull(state, ::renderState)
            viewLifecycleOwner.observeNotNull(capturingMedia, ::captureMedia)
            viewLifecycleOwner.observeNotNull(exit, { findNavController().popBackStack() })
            viewLifecycleOwner.observeNotNull(gpsEnabled, ::proceedGpsRequiredAction)
        }
        observeMeasurementDisposeDecisionResults()
        observeGpsRequestDialogResults()
    }

    private fun renderState(state: CreateMeasurementUiState) = with(state) {
        createMeasurementAdapter.setDataWithState(
            data = fields,
            error = error,
            isLoading = isLoading,
            isNextPageLoading = false,
            isNextPageError = false,
            isPrevPageLoading = false,
            isPrevPageError = false
        )
        navigateToPreview.isEnabled = state.isReady && !isLoading
        backToStartMeasurement.isEnabled = !isLoading
    }

    private fun captureMedia(capturingMedia: CapturingMedia) {
        selectedMediaCategory = null
        when (capturingMedia.measurementMediaCategory) {
            MeasurementMediaCategory.VIDEO -> {
                captureVideoCall.launch(capturingMedia.outputUri)
            }
            MeasurementMediaCategory.ACT_PHOTO,
            MeasurementMediaCategory.PHOTO -> {
                capturePhotoCall.launch(capturingMedia.outputUri)
            }
        }
    }

    private fun exitWithSaveMeasurementCheck() {
        viewModel.exitWithSaveCheck()
    }

    private fun observeMeasurementDisposeDecisionResults() {
        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>(DIALOG_ACTION_KEY)
            ?.let { liveData ->
                viewLifecycleOwner.observeNotNull(liveData) { action ->
                    when (action) {
                        DisposeMeasurementCardDialogFragment.DIALOG_LEAVE_ACTION -> {
                            viewModel.clearData()
                        }
                    }
                }
            }
    }

    private fun observeGpsRequestDialogResults() {
        GpsRequestDialogDelegate(findNavController(), viewLifecycleOwner)
            .observeGpsRequestDialogResults { gpsRequestResultApproved ->
                if (gpsRequestResultApproved) {
                    openLocationSettingsCall.launch()
                } else {
                    viewModel.gpsEnableRequestRejected()
                    proceedGpsRequiredAction(isGpsEnabled = false)
                }
            }
    }

    private fun getSpaceItemDecoration(): RecyclerView.ItemDecoration {
        val margin = requireContext().resources
            .getDimensionPixelSize(R.dimen.list_item_padding)
        return MarginInternalItemDecoration(
            spaceHeight = margin,
            needSpace = { holder ->
                holder !is CardEmptyLineAdapterDelegate.Holder &&
                    holder !is CardBottomCornersAdapterDelegate.Holder &&
                    holder !is CardDividerAdapterDelegate.Holder &&
                    holder !is MorphologyCardAdapterDelegate.Holder &&
                    holder !is CardTitleItemWitAccentAdapterDelegate.Holder
            }
        )
    }

    private enum class GpsRequiredAction {
        CREATE_MEASUREMENT,
        CAPTURE_MEDIA,
        COMPLETED
    }

    private fun checkPermissionGranted(permission: String): Boolean {
        return PermissionChecker.checkSelfPermission(
            requireContext(),
            permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    @StringRes
    private fun mapPermissionToRationale(permission: String): Int? {
        return when (permission) {
            Manifest.permission.CAMERA -> R.string.dialog_media_permission_camera_rationale
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> R.string.dialog_media_permission_storage_rationale
            Manifest.permission.ACCESS_FINE_LOCATION -> R.string.dialog_media_permission_location_rationale
            Manifest.permission.ACCESS_COARSE_LOCATION -> null
            else -> error("Unreachable condition")
        }
    }

    private fun prepareMediaPermissionCallback(
        permissions: Array<String>,
        requiresPermission: () -> Unit
    ): MultiplePermissionsContract {
        return MultiplePermissionsContract(
            this,
            permissions,
            { requiresPermission() },
            { request -> showRationale(permissions, request) }
        )
    }

    companion object {
        private const val LOW_QUALITY = 0
        private const val FIVE_MINUTES = 5 * 60
        const val DIALOG_ACTION_KEY: String = "DIALOG_ACTION_KEY"
        private val PERMISSIONS_FOR_MEDIA = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        private val PERMISSIONS_LOCATION = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private val PERMISSIONS_FOR_DOCUMENTS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}
