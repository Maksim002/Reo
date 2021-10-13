package ru.ktsstudio.common.ui.permissions

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * Created by Igor Park on 27/11/2020.
 */
class MultiplePermissionsContract(
    fragment: Fragment,
    private val permissions: Array<String>,
    private val requiresPermission: Fun,
    private val onShowRationale: ShowRationaleFun
) {
    private val permissionsResultCallback = fragment.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsState ->
        if (permissionsState.all { it.value }) {
            requiresPermission.invoke()
        } else {
            onShowRationale(
                KtxPermissionRequest.create(
                    onPermissionDenied = null,
                    requestPermission = { launch() }
                )
            )
        }
    }

    fun launch() {
        permissionsResultCallback.launch(permissions)
    }
}
