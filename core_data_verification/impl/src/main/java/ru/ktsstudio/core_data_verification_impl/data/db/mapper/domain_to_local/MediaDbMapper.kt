package ru.ktsstudio.core_data_verification_impl.data.db.mapper.domain_to_local

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.Media
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalMedia
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 15.12.2020.
 */
class MediaDbMapper @Inject constructor() : Mapper<Media, LocalMedia> {
    override fun map(item: Media): LocalMedia = with(item) {
        LocalMedia(
            remoteId = remoteId,
            localFilePath = cachedFile?.toURI()?.path,
            gpsPoint = gpsPoint,
            date = date
        )
    }
}