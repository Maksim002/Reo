package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import org.threeten.bp.Instant
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import ru.ktsstudio.core_network_api.qualifiers.ApiUrl
import ru.ktsstudio.utilities.extensions.orDefault
import java.io.File
import javax.inject.Inject

/**
 * Created by Igor Park on 11/12/2020.
 */
class MediaMapper @Inject constructor(
    @ApiUrl private val apiUrl: String
) : Mapper<SerializableMedia, Media> {
    override fun map(item: SerializableMedia): Media = with(item) {
        return Media(
            remoteId = remoteId,
            remoteUrl = remoteId?.let { "${apiUrl}api/file/image/$it" },
            cachedFile = localPath?.let(::File),
            gpsPoint = GpsPoint(
                lat = latitude?.toDoubleOrNull().orDefault(0.0),
                lng = longitude?.toDoubleOrNull().orDefault(0.0)
            ),
            date = Instant.ofEpochMilli(date)
        )
    }
}