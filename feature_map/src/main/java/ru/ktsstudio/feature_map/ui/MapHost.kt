package ru.ktsstudio.feature_map.ui

import androidx.fragment.app.Fragment

/**
 * Created by Igor Park on 05/10/2020.
 */
interface MapHost {
    fun openObjectsInfo(objectIds: List<String>)
    fun openGeoDataRequestDialog()
    fun observeGpsRequestDialogResults(childFragment: Fragment, result: (Boolean) -> Unit)
}