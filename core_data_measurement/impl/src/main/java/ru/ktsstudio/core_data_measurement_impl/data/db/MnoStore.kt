package ru.ktsstudio.core_data_measurement_impl.data.db

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.utils.db.batchedObserve
import ru.ktsstudio.common.utils.db.batchedQuerySingle
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.CategoryDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.ContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementContainerDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MeasurementDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MnoDao
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalCategory
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMno
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoWithRelations
import javax.inject.Inject

class MnoStore @Inject constructor(
    private val db: MeasurementDb,
    private val mnoDao: MnoDao,
    private val containerDao: ContainerDao,
    private val categoryDao: CategoryDao,
    private val measurementDao: MeasurementDao,
    private val measurementContainerDao: MeasurementContainerDao
) {

    fun clear(): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                containerDao.clear().blockingAwait()
                measurementContainerDao.clearContainerWasteTypes().blockingAwait()
                measurementContainerDao.clearMixedContainers().blockingAwait()
                measurementContainerDao.clearSeparateWasteContainers().blockingAwait()
                measurementDao.clearMeasurementMedia().blockingAwait()
                measurementDao.clearMeasurements().blockingAwait()
                mnoDao.clear().blockingAwait()
            }
        }
    }

    fun saveMnosWitRelations(
        mnoList: List<LocalMno>,
        containerList: List<LocalMnoContainer>,
        categoryList: List<LocalCategory>
    ): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                categoryDao.insert(categoryList).blockingAwait()
                mnoDao.insert(mnoList).blockingAwait()
                containerDao.insert(containerList).blockingAwait()
            }
        }
    }

    fun observeAllMno(): Observable<List<LocalMnoWithRelations>> {
        return mnoDao.observeAll()
    }

    fun observeMnoById(id: String): Observable<LocalMnoWithRelations> {
        return mnoDao.observeById(id)
    }

    fun observeMnoListByIds(ids: List<String>): Observable<List<LocalMnoWithRelations>> {
        return ids.batchedObserve(mnoDao::observeByIds)
    }

    fun getMnoListByIds(ids: List<String>): Single<List<LocalMnoWithRelations>> {
        return ids.batchedQuerySingle(mnoDao::getByIds)
    }

    fun getMnoContainersByMnoId(mnoId: String): Single<List<LocalMnoContainerWithRelations>> {
        return mnoDao.getMnoContainersWithRelationsById(mnoId)
    }

    fun getMnoContainerById(containerId: String): Maybe<LocalMnoContainerWithRelations> {
        return mnoDao.getMnoContainerWithRelationsById(containerId)
    }

    fun getMnoById(id: String): Maybe<LocalMnoWithRelations> {
        return mnoDao.getById(id)
    }

    fun getTaskIdForMno(mnoId:String): Maybe<String> {
        return mnoDao.getById(mnoId)
            .map { it.mno.taskIds.first() }
    }
}