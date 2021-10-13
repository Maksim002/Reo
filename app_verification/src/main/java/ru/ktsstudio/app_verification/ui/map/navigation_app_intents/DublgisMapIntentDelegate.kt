package ru.ktsstudio.app_verification.ui.map.navigation_app_intents

import android.content.Intent
import androidx.core.net.toUri
import ru.ktsstudio.app_verification.domain.models.Route

/**
 * @author Maxim Ovchinnikov on 17.11.2020.
 */
class DublgisMapIntentDelegate(private val route: Route) : MapIntentDelegate() {

    override fun getMapIntent(): Intent {
        val routeStartFormatted = "${route.start.lng},${route.start.lat}"
        val roundEndFormatted = "${route.destination.lng},${route.destination.lat}"
        val routeFormatted = "$routeStartFormatted/to/$roundEndFormatted"

        val dublgisMapUri = "$DUBLGIS_MAP_DESTINATION_URL$routeFormatted".toUri()
        return Intent(Intent.ACTION_VIEW, dublgisMapUri).apply {
            setPackage(DUBLGIS_MAP_PACKAGE)
        }
    }

    companion object {
        private const val DUBLGIS_MAP_DESTINATION_URL = "dgis://2gis.ru/routeSearch/rsType/car/from/"
        private const val DUBLGIS_MAP_PACKAGE = "ru.dublgis.dgismobile"
    }
}
