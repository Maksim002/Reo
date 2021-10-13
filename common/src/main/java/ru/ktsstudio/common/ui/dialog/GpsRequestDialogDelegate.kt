package ru.ktsstudio.common.ui.dialog

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import ru.ktsstudio.common.utils.observeNotNull

/**
 * @author Maxim Ovchinnikov on 08.02.2021.
 */
class GpsRequestDialogDelegate(
    private val navController: NavController,
    private val viewLifecycleOwner: LifecycleOwner
) {
    fun observeGpsRequestDialogResults(result: (Boolean) -> Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.also { it.remove<String>(GpsRequestDialogFragment.DIALOG_ENABLE_GPS_REQUEST_KEY) }
            ?.getLiveData<String>(GpsRequestDialogFragment.DIALOG_ENABLE_GPS_REQUEST_KEY)
            ?.let { liveData ->
                viewLifecycleOwner.observeNotNull(liveData) { action ->
                    result(action == GpsRequestDialogFragment.DIALOG_GEO_DATA_APPROVE)
                }
            }
    }
}
