package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.common.utils.mapper.Mapper2
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verification_impl.data.network.model.GpsPointToSend
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import ru.ktsstudio.core_data_verification_impl.data.network.model.SerializableMedia
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.12.2020.
 */
class MediaToSendMapper @Inject constructor(
    private val gpsPointSendMapper: Mapper<GpsPoint, GpsPointToSend>
) : Mapper2<Media, @JvmSuppressWildcards Map<String, LocalMedia>, SerializableMedia> {
    override fun map(item1: Media, item2: Map<String, LocalMedia>): SerializableMedia = with(item1) {
        val localPath = cachedFile?.path
        val gpsPointToSend = gpsPointSendMapper.map(gpsPoint)
        val localMedia = localPath?.let { item2[it] }
        return SerializableMedia(
            remoteId = remoteId ?: localMedia?.remoteId,
            localPath = localPath,
            latitude = gpsPointToSend.lat,
            longitude = gpsPointToSend.lng,
            date = date.toEpochMilli()
        )
    }
}