package ru.ktsstudio.app_verification.ui.map.navigation_app_intents

import android.content.Intent
import androidx.core.net.toUri
import ru.ktsstudio.app_verification.domain.models.Route

/**
 * @author Maxim Ovchinnikov on 17.11.2020.
 */
class GoogleMapIntentDelegate(private val route: Route) : MapIntentDelegate() {

    override fun getMapIntent(): Intent {
        val routeFormatted = "${route.destination.lat},${route.destination.lng}"
        val googleMapUri = "$GOOGLE_MAP_DESTINATION_URL$routeFormatted".toUri()

        return Intent(Intent.ACTION_VIEW, googleMapUri).apply {
            setPackage(GOOGLE_MAP_PACKAGE)
        }
    }

    companion object {
        private const val GOOGLE_MAP_DESTINATION_URL = "https://www.google.com/maps/dir/?api=1&destination="
        private const val GOOGLE_MAP_PACKAGE = "com.google.android.apps.maps"
    }
}
