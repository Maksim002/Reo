package ru.ktsstudio.core_data_measurement_impl.data.db

import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.ContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementDao
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalContainerType
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMeasurementStatus
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteCategory
import javax.inject.Inject

/**
 * @author Maxim Ovchinnikov on 15.10.2020.
 */
class ContainerStore @Inject constructor(
    private val db: MeasurementDb,
    private val measurementContainerDao: MeasurementContainerDao,
    private val measurementDao: MeasurementDao,
    private val containerDao: ContainerDao,
) {

    fun clear(): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                containerDao.clear().blockingAwait()
                measurementContainerDao.clearWasteCategories().blockingAwait()
                measurementContainerDao.clearContainerWasteTypes().blockingAwait()
                measurementContainerDao.clearSeparateWasteContainers().blockingAwait()
                measurementContainerDao.clearMixedContainers().blockingAwait()
                measurementContainerDao.clearContainerTypes().blockingAwait()
            }
        }
    }

    fun saveRegister(
        containerTypes: List<LocalContainerType>,
        wasteCategories: List<LocalWasteCategory>,
        measurementStatuses: List<LocalMeasurementStatus>
    ): Completable {
        return Completable.fromCallable {
            measurementContainerDao.insertLocalContainerTypes(containerTypes).blockingAwait()
            measurementContainerDao.insertLocalWasteCategories(wasteCategories).blockingAwait()
            measurementDao.insertMeasurementStatuses(measurementStatuses).blockingAwait()
        }
    }

    fun saveContainerTypes(containerTypes: List<LocalContainerType>): Completable {
        return measurementContainerDao.insertLocalContainerTypes(containerTypes)
    }
}