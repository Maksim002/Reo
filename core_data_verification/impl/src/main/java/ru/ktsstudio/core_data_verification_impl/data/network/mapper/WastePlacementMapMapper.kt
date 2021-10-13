package ru.ktsstudio.core_data_verification_impl.data.network.mapper

import org.threeten.bp.Instant
import ru.ktsstudio.common.utils.mapper.Mapper
import ru.ktsstudio.core_data_verfication_api.data.model.technical.WastePlacementMap
import ru.ktsstudio.core_data_verification_impl.data.network.model.RemoteWastePlacementMap
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 16.01.2021.
 */
class WastePlacementMapMapper @Inject constructor() : Mapper<RemoteWastePlacementMap, WastePlacementMap> {
    override fun map(item: RemoteWastePlacementMap): WastePlacementMap = with(item) {
        return WastePlacementMap(
            id = id,
            commissioningPeriod = period?.let(Instant::ofEpochMilli),
            area = area
        )
    }
}