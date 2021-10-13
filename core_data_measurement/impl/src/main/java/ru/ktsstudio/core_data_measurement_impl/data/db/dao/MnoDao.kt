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
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMno
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainer
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainerWithRelations
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoWithRelations

@Dao
interface MnoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mnos: List<LocalMno>): Completable

    @Query("DELETE FROM ${LocalMno.TABLE_NAME}")
    fun clear(): Completable

    @Transaction
    @Query("SELECT * FROM ${LocalMno.TABLE_NAME} WHERE ${LocalMno.COLUMN_ID} = :id")
    fun getById(id: String): Maybe<LocalMnoWithRelations>

    @Transaction
    @Query("SELECT * FROM ${LocalMnoContainer.TABLE_NAME} WHERE ${LocalMnoContainer.COLUMN_MNO_ID} = :mnoId")
    fun getMnoContainersWithRelationsById(mnoId: String): Single<List<LocalMnoContainerWithRelations>>

    @Transaction
    @Query("SELECT * FROM ${LocalMnoContainer.TABLE_NAME} WHERE ${LocalMnoContainer.COLUMN_ID} = :containerId")
    fun getMnoContainerWithRelationsById(containerId: String): Maybe<LocalMnoContainerWithRelations>

    @Transaction
    @Query("SELECT * FROM ${LocalMno.TABLE_NAME}")
    fun observeAll(): Observable<List<LocalMnoWithRelations>>

    @Transaction
    @Query("SELECT * FROM ${LocalMno.TABLE_NAME} WHERE ${LocalMno.COLUMN_ID} = :id")
    fun observeById(id: String): Observable<LocalMnoWithRelations>

    @Transaction
    @Query(QUERY_BY_IDS)
    fun observeByIds(ids: List<String>): Observable<List<LocalMnoWithRelations>>

    @Transaction
    @Query(QUERY_BY_IDS)
    fun getByIds(ids: List<String>): Single<List<LocalMnoWithRelations>>

    companion object {
        private const val QUERY_BY_IDS = "SELECT * FROM ${LocalMno.TABLE_NAME} WHERE ${LocalMno.COLUMN_ID} IN (:ids)"
    }
}