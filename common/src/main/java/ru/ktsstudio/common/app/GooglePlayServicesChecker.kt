package ru.ktsstudio.common.app

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import ru.ktsstudio.common.R
import ru.ktsstudio.common.ui.dialog.ConfirmDialogFragment

/**
 * @author Maxim Myalkin (MaxMyalkin) on 02.11.2020.
 */
class GooglePlayServicesChecker {

    fun check(activity: AppCompatActivity) {
        val googleApiAvailability = GoogleApiAvailability.getInstance()

        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity)

        if (resultCode != ConnectionResult.SUCCESS)
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(
                    activity,
                    resultCode,
                    GOOGLE_SERVICES_API_REQUEST
                )
                    .apply {
                        setTitle(R.string.google_play_unavailable_title)
                        setCancelable(false)
                    }
                    .show()
            } else {
                ConfirmDialogFragment.getInstance(
                    activity.getString(R.string.google_play_unavailable_title),
                    activity.getString(R.string.google_play_unavailable_message),
                    tag = "GPUnavailableTag"
                )
                    .apply { isCancelable = false }
                    .show(activity.supportFragmentManager, "GPUnavailableTag")
            }
    }

    companion object {
        private const val GOOGLE_SERVICES_API_REQUEST = 12
    }
}