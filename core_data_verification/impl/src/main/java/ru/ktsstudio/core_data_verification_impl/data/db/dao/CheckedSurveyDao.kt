package ru.ktsstudio.core_data_verification_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalCheckedSurvey

/**
 * @author Maxim Ovchinnikov on 26.11.2020.
 */
@Dao
interface CheckedSurveyDao {

    @Query("DELETE FROM ${LocalCheckedSurvey.TABLE_NAME}")
    fun clear(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalCheckSurveys(checkedSurveys: List<LocalCheckedSurvey>): Completable

    @Query("SELECT * FROM ${LocalCheckedSurvey.TABLE_NAME} WHERE ${LocalCheckedSurvey.COLUMN_OBJECT_ID} = :id")
    fun getById(id: String): Maybe<LocalCheckedSurvey>

    @Query("SELECT * FROM ${LocalCheckedSurvey.TABLE_NAME} WHERE ${LocalCheckedSurvey.COLUMN_OBJECT_ID} = :objectId")
    fun observeCheckedSurvey(objectId: String): Observable<LocalCheckedSurvey>
}