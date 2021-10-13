package ru.ktsstudio.core_data_measurement_impl.data.db

import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MorphologyDao
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.WasteGroupDao
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteSubgroup
import javax.inject.Inject

class WasteGroupStore @Inject constructor(
    private val db: MeasurementDb,
    private val wasteGroupDao: WasteGroupDao,
    private val morphologyDao: MorphologyDao
) {

    fun clear(): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                wasteGroupDao.clearGroups().blockingAwait()
                wasteGroupDao.clearSubgroups().blockingAwait()
                morphologyDao.clear().blockingAwait()
            }
        }
    }

    fun saveRegister(
        wasteGroups: List<LocalWasteGroup>,
        wasteSubgroups: List<LocalWasteSubgroup>
    ): Completable {
        return Completable.fromCallable {
            db.runInTransaction {
                wasteGroupDao.insertWasteGroups(wasteGroups).blockingAwait()
                wasteGroupDao.insertWasteSubroups(wasteSubgroups).blockingAwait()
            }
        }
    }
}