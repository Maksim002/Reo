package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verification_impl.data.network.model.GpsPointToSend
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 17.02.2021.
 */
class GpsPointToSendMapper @Inject constructor(): Mapper<GpsPoint, GpsPointToSend> {
    override fun map(item: GpsPoint): GpsPointToSend {
        return GpsPointToSend(
            lat = item.lat.toLimitedString(),
            lng = item.lng.toLimitedString()
        )
    }

    private fun Double.toLimitedString(): String = toString().take(MAX_GPS_LENGTH)

    companion object {
        private const val MAX_GPS_LENGTH = 16
    }
}