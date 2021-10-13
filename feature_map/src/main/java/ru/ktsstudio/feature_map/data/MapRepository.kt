package ru.ktsstudio.feature_map.data

import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.models.MapObject

/**
 * @author Maxim Ovchinnikov on 13.11.2020.
 */
interface MapRepository {

    fun getRecycleObjectsForArea(from: GpsPoint, to: GpsPoint, filter: Filter): Single<List<MapObject>>
}