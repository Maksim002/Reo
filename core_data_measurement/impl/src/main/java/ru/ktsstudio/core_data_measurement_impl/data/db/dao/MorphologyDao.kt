package ru.ktsstudio.core_data_measurement_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphology
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMorphologyWithRelation
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroupWithRelations

@Dao
interface MorphologyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMorphologies(categories: List<LocalMorphology>): Completable

    @Transaction
    @Query("""SELECT * FROM ${LocalMorphology.TABLE_NAME}
                    WHERE measurementId = :measurementId
                    AND draftState != '"DELETED"'""")
    fun observeMorphologiesBy(measurementId: Long): Observable<List<LocalMorphologyWithRelation>>

    @Transaction
    @Query("SELECT * FROM ${LocalMorphology.TABLE_NAME} WHERE ${LocalMorphology.COLUMN_LOCAL_ID} = :localId")
    fun getMorphologyBy(localId: Long): Maybe<LocalMorphologyWithRelation>

    @Transaction
    @Query("SELECT * FROM ${LocalWasteGroup.TABLE_NAME}")
    fun getWasteGroupsWithRelations(): Single<List<LocalWasteGroupWithRelations>>

    @Query("DELETE FROM ${LocalMorphology.TABLE_NAME} WHERE ${LocalMorphology.COLUMN_MEASUREMENT_LOCAL_ID} = :measurementId")
    fun deleteCategoriesByMeasurementId(measurementId: Long): Completable

    @Query("DELETE FROM ${LocalMorphology.TABLE_NAME} WHERE ${LocalMorphology.COLUMN_LOCAL_ID}  = :categoryLocalId")
    fun deleteMorphologiesById(categoryLocalId: Long): Completable

    @Query("DELETE FROM ${LocalMorphology.TABLE_NAME}")
    fun clear(): Completable
}
