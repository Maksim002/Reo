package ru.ktsstudio.app_verification.ui.map.navigation_app_intents

import android.app.Activity
import android.content.Intent
import ru.ktsstudio.common.utils.checkAvailableReceiver

/**
 * @author Maxim Ovchinnikov on 17.11.2020.
 */
abstract class MapIntentDelegate {

    abstract fun getMapIntent(): Intent

    fun openMap(activity: Activity): Boolean {
        return openMapApp(activity, getMapIntent())
    }

    private fun openMapApp(activity: Activity, intent: Intent): Boolean {

        fun isAppInstalled(): Boolean {
            return intent.checkAvailableReceiver(activity.packageManager) != null
        }

        if (isAppInstalled().not()) return false
        activity.startActivity(intent)
        return true
    }
}
