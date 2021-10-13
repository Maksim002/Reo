package ru.ktsstudio.app_verification.data.map

import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.feature_map.data.MapRepository
import ru.ktsstudio.common.data.models.GpsPoint
import ru.ktsstudio.common.domain.filter.Filter
import ru.ktsstudio.common.domain.models.MapObject
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_verfication_api.data.ObjectRepository
import ru.ktsstudio.core_data_verfication_api.data.model.VerificationObjectFilterApplier
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 13.11.2020.
 */
class ObjectMapRepositoryImpl @Inject constructor(
    private val schedulers: SchedulerProvider,
    private val objectRepository: ObjectRepository,
    private val verificationObjectFilterApplier: VerificationObjectFilterApplier
) : MapRepository {

    override fun getRecycleObjectsForArea(
        from: GpsPoint,
        to: GpsPoint,
        filter: Filter
    ): Single<List<MapObject>> {
        return objectRepository.observeAllObjects()
            .firstOrError()
            .map<List<MapObject>> { objectList ->
                objectList.filter {
                    verificationObjectFilterApplier.applyFilter(it, filter)
                }
                    .map {
                        VerificationMapObject(
                            id = it.id,
                            latitude = it.gpsPoint.lat,
                            longitude = it.gpsPoint.lng
                        )
                    }
            }
            .subscribeOn(schedulers.io)
    }
}
