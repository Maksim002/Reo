package ru.ktsstudio.core_data_verification_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_verfication_api.data.model.reference.ReferenceType
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalReference

/**
 * Created by Igor Park on 11/12/2020.
 */
@Dao
interface ReferenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(references: List<LocalReference>): Completable

    @Query("DELETE FROM ${LocalReference.TABLE_NAME}")
    fun clear(): Completable

    @Query("SELECT * FROM ${LocalReference.TABLE_NAME}")
    fun getAllReferences(): Single<List<LocalReference>>

    @Query("SELECT * FROM ${LocalReference.TABLE_NAME} WHERE ${LocalReference.COLUMN_REFERENCE_TYPE} = :type")
    fun getReferencesByType(type: ReferenceType): Single<List<LocalReference>>
}