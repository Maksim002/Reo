package ru.ktsstudio.core_data_verification_impl.data.network.mapper.send

import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWastePlacementMap
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 16.01.2021.
 */
class WastePlacementMapToSendMapper @Inject constructor() : Mapper<WastePlacementMap, RemoteWastePlacementMap> {
    override fun map(item: WastePlacementMap): RemoteWastePlacementMap = with(item) {
        return RemoteWastePlacementMap(
            id = id,
            period = commissioningPeriod?.toEpochMilli(),
            area = area
        )
    }
}