package ru.ktsstudio.app_verification.ui.map.navigation_app_intents

import android.content.Intent
import androidx.core.net.toUri
import ru.ktsstudio.app_verification.domain.models.Route

/**
 * @author Maxim Ovchinnikov on 17.11.2020.
 */
class YandexMapIntentDelegate(private val route: Route) : MapIntentDelegate() {

    override fun getMapIntent(): Intent {
        val routeStartFormatted = "${route.start.lat},${route.start.lng}"
        val roundEndFormatted = "${route.destination.lat},${route.destination.lng}"
        val routeFormatted = "$routeStartFormatted~$roundEndFormatted"

        val yandexMapUri = "$YANDEX_MAP_DESTINATION_URL$routeFormatted".toUri()
        return Intent(Intent.ACTION_VIEW, yandexMapUri).apply {
            setPackage(YANDEX_MAP_PACKAGE)
        }
    }

    companion object {
        private const val YANDEX_MAP_DESTINATION_URL = "yandexmaps://maps.yandex.ru/?rtext="
        private const val YANDEX_MAP_PACKAGE = "ru.yandex.yandexmaps"
    }
}
