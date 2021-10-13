package ru.ktsstudio.core_data_measurement_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalMnoContainer

@Dao
interface ContainerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(containers: List<LocalMnoContainer>): Completable

    @Query("DELETE FROM ${LocalMnoContainer.TABLE_NAME}")
    fun clear(): Completable
}