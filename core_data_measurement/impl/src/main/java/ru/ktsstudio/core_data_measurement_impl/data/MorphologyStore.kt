package ru.ktsstudio.core_data_measurement_impl.data

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_measurement_impl.data.db.dao.MorphologyDao
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphology
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphologyWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroupWithRelations
import javax.inject.Inject

class MorphologyStore @Inject constructor(
    private val morphologyDao: MorphologyDao
) {
    fun saveMorphologies(categories: List<LocalMorphology>): Completable {
        return morphologyDao.insertMorphologies(categories)
    }

    fun deleteMorphology(localId: Long): Completable {
        return morphologyDao.deleteMorphologiesById(localId)
    }

    fun getMorphologyById(categoryId: Long): Maybe<LocalMorphologyWithRelation> {
        return morphologyDao.getMorphologyBy(categoryId)
    }

    fun getCompositeWasteGroups(): Single<List<LocalWasteGroupWithRelations>>{
        return morphologyDao.getWasteGroupsWithRelations()
    }

    fun observeMorphologiesByMeasurementId(
        measurementId: Long
    ): Observable<List<LocalMorphologyWithRelation>> {
        return morphologyDao.observeMorphologiesBy(measurementId)
    }
}