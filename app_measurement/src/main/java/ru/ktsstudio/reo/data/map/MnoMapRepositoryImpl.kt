package ru.ktsstudio.reo.data.map

import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.feature_map.data.MapRepository
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.models.MapObject
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.MnoRepository
import ru.ktsstudio.core_data_measurement_api.data.model.MnoFilterApplier
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 13.11.2020.
 */
class MnoMapRepositoryImpl @Inject constructor(
    private val schedulers: SchedulerProvider,
    private val mnoRepository: MnoRepository,
    private val mnoFilterApplier: MnoFilterApplier
) : MapRepository {

    override fun getRecycleObjectsForArea(
        from: GpsPoint,
        to: GpsPoint,
        filter: Filter
    ): Single<List<MapObject>> {
        return mnoRepository.observeMnoList()
            .firstOrError()
            .map<List<MapObject>> { mnoList ->
                mnoList.filter {
                    mnoFilterApplier.applyFilter(it, filter)
                }
                    .map {
                        MnoMapObject(
                            id = it.objectInfo.mnoId,
                            latitude = it.objectInfo.gpsPoint.lat,
                            longitude = it.objectInfo.gpsPoint.lng
                        )
                    }
            }
            .subscribeOn(schedulers.io)
    }
}
