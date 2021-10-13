package ru.ktsstudio.core_data_verification_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObject
import ru.ktsstudio.core_data_verification_impl.data.db.model.LocalVerificationObjectWithRelation

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.11.2020.
 */
@Dao
interface VerificationObjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalVerificationObject(objects: List<LocalVerificationObject>): Completable

    @Query("DELETE FROM ${LocalVerificationObject.TABLE_NAME}")
    fun clearObjects(): Completable

    @Transaction
    @Query("SELECT * FROM ${LocalVerificationObject.TABLE_NAME}")
    fun observeAllObjects(): Observable<List<LocalVerificationObjectWithRelation>>

    @Transaction
    @Query("SELECT * FROM ${LocalVerificationObject.TABLE_NAME} WHERE ${LocalVerificationObject.COLUMN_ID} IN (:ids)")
    fun getByIds(ids: List<String>): Single<List<LocalVerificationObjectWithRelation>>

    @Transaction
    @Query("SELECT * FROM ${LocalVerificationObject.TABLE_NAME} WHERE ${LocalVerificationObject.COLUMN_ID} = :id")
    fun getById(id: String): Maybe<LocalVerificationObjectWithRelation>

    @Transaction
    @Query("""SELECT * FROM ${LocalVerificationObject.TABLE_NAME} WHERE ${LocalVerificationObject.COLUMN_LOCAL_STATE} != '"SUCCESS"'""")
    fun getNotSyncedObjects(): Single<List<LocalVerificationObjectWithRelation>>
}