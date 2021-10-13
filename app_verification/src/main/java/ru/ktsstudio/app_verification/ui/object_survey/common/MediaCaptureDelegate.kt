package ru.ktsstudio.app_verification.ui.object_survey.common

import android.Manifest
import android.content.res.Resources
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.launch
import androidx.annotation.StringRes
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.fragment.findNavController
import permissions.dispatcher.PermissionRequest
import ru.ktsstudio.app_verification.R
import ru.ktsstudio.app_verification.domain.models.CapturingMedia
import ru.ktsstudio.app_verification.presentation.media.MediaSurveyViewModel
import ru.ktsstudio.common.ui.dialog.GpsRequestDialogFragment.Companion.DIALOG_ENABLE_GPS_REQUEST_KEY
import ru.ktsstudio.common.ui.OpenSettingsContract
import ru.ktsstudio.common.ui.TakePicture
import ru.ktsstudio.common.ui.TakePictureResult
import ru.ktsstudio.common.ui.dialog.GpsRequestDialogFragment
import ru.ktsstudio.common.ui.permissions.MultiplePermissionsContract
import ru.ktsstudio.common.utils.exhaustive
import ru.ktsstudio.common.utils.observeNotNull
import ru.ktsstudio.common.utils.showRationaleDialog
import ru.ktsstudio.utilities.extensions.toast
import kotlin.properties.Delegates

/**
 * @author Maxim Ovchinnikov on 12.12.2020.
 */
internal class MediaCaptureDelegate<DraftType>(
    private val fragment: Fragment,
    private val mediaViewModel: MediaSurveyViewModel<DraftType>
) : LifecycleObserver {
    private val resources: Resources
        get() = fragment.resources

    private val capturePhotoCall = fragment.registerForActivityResult(
        TakePicture()
    ) { result ->
        when (result) {
            is TakePictureResult.Success -> mediaViewModel.mediaCaptured()
            is TakePictureResult.Fail -> result.uri?.let(mediaViewModel::cancelPhotoCapture)
        }
    }

    private var gpsRequiredAction: GpsRequiredAction by Delegates.observable(
        GpsRequiredAction.COMPLETED
    ) { _, _, newValue ->
        if (newValue != GpsRequiredAction.COMPLETED) mediaViewModel.checkGpsState()
    }

    private val captureMediaPermissionCall = MultiplePermissionsContract(
        fragment,
        PERMISSIONS_FOR_MEDIA,
        { gpsRequiredAction = GpsRequiredAction.CAPTURE_MEDIA },
        { request -> showRationale(request) }
    )

    private val openLocationSettingsCall = fragment.registerForActivityResult(
        OpenSettingsContract(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    ) { proceedGpsRequiredAction(isGpsEnabled = true) }

    init {
        observeViewModel()
    }

    private fun observeViewModel() = with(mediaViewModel) {
        fragment.viewLifecycleOwner.observeNotNull(mediaViewModel.gpsEnabled, ::proceedGpsRequiredAction)
        fragment.viewLifecycleOwner.observeNotNull(mediaViewModel.gpsEnableRequest) {
            val bundle = Bundle().apply {
                putString(
                    GpsRequestDialogFragment.MESSAGE_ARG_KEY,
                    resources.getString(R.string.geo_data_request_dialog_message_photo_creation)
                )
            }
            fragment.findNavController().navigate(R.id.gpsRequestDialogFragment, bundle)
        }
        fragment.viewLifecycleOwner.observeNotNull(mediaViewModel.capturingMedia, ::captureMedia)
        fragment.viewLifecycleOwner
            .observeNotNull(mediaViewModel.capturingMediaPermissionCheck) { checkMediaPermission() }
        observeGpsRequestDialogResults()
    }

    private fun checkMediaPermission() {
        captureMediaPermissionCall.launch()
    }

    private fun proceedGpsRequiredAction(isGpsEnabled: Boolean) {
        if (isGpsEnabled) {
            when (gpsRequiredAction) {
                GpsRequiredAction.CAPTURE_MEDIA -> {
                    mediaViewModel.captureMediaRequest()
                }
                GpsRequiredAction.COMPLETED -> Unit
            }.exhaustive
        } else {
            fragment.toast(resources.getString(R.string.geo_data_empty))
        }
        gpsRequiredAction = GpsRequiredAction.COMPLETED
    }

    private fun captureMedia(capturingMedia: CapturingMedia) {
        capturePhotoCall.launch(capturingMedia.outputUri)
    }

    private fun showRationale(request: PermissionRequest) {
        val dialogMessage = StringBuilder().apply {
            appendLine(resources.getString(R.string.dialog_permission_title))
            PERMISSIONS_FOR_MEDIA.forEach { permission ->
                val rationaleMessageRes = mapPermissionToRationale(permission)
                if (checkPermissionGranted(permission).not() && rationaleMessageRes != null) {
                    appendLine(resources.getString(rationaleMessageRes))
                }
            }
        }.toString()

        fragment.context?.showRationaleDialog(
            dialogMessage = dialogMessage,
            request = request
        )
    }

    @StringRes
    private fun mapPermissionToRationale(permission: String): Int? {
        return when (permission) {
            Manifest.permission.CAMERA -> R.string.dialog_media_permission_camera_rationale
            Manifest.permission.ACCESS_FINE_LOCATION -> R.string.dialog_media_permission_location_rationale
            Manifest.permission.ACCESS_COARSE_LOCATION -> null
            else -> error("Unreachable condition")
        }
    }

    private fun checkPermissionGranted(permission: String): Boolean {
        return PermissionChecker.checkSelfPermission(
            fragment.requireContext(),
            permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    private fun observeGpsRequestDialogResults() {
        fragment.findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>(DIALOG_ENABLE_GPS_REQUEST_KEY)
            ?.let { liveData ->
                fragment.viewLifecycleOwner.observeNotNull(liveData) { action ->
                    when (action) {
                        GpsRequestDialogFragment.DIALOG_GEO_DATA_APPROVE -> {
                            openLocationSettingsCall.launch()
                        }
                        GpsRequestDialogFragment.DIALOG_GEO_DATA_REJECT -> {
                            fragment.toast(resources.getString(R.string.geo_data_request_reject_toast_photo_creation))
                        }
                    }
                }
            }
    }

    private enum class GpsRequiredAction {
        CAPTURE_MEDIA,
        COMPLETED
    }

    companion object {
        private val PERMISSIONS_FOR_MEDIA = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
