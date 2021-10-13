package ru.ktsstudio.app_verification.ui.map.navigation_app_intents

import android.content.Intent
import ru.ktsstudio.app_verification.domain.models.Route

/**
 * @author Maxim Ovchinnikov on 17.11.2020.
 */
class MapsMeMapIntentDelegate(private val route: Route) : MapIntentDelegate() {

    override fun getMapIntent(): Intent {
        return Intent(MAPSME_MAP_DESTINATION_ACTION).apply {
            setPackage(MAPSME_MAP_PACKAGE)
            putExtra(LAT_FROM_EXTRA, route.start.lat)
            putExtra(LON_FROM_EXTRA, route.start.lng)
            putExtra(LAT_TO_EXTRA, route.destination.lat)
            putExtra(LON_TO_EXTRA, route.destination.lng)
        }
    }

    companion object {
        private const val MAPSME_MAP_DESTINATION_ACTION = "com.mapswithme.maps.pro.action.BUILD_ROUTE"
        private const val MAPSME_MAP_PACKAGE = "com.mapswithme.maps.pro"
        private const val LAT_FROM_EXTRA = "lat_from"
        private const val LON_FROM_EXTRA = "lon_from"
        private const val LAT_TO_EXTRA = "lat_to"
        private const val LON_TO_EXTRA = "lon_to"
    }
}
