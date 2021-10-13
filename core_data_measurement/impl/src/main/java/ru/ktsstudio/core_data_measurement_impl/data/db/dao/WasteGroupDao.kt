package ru.ktsstudio.core_data_measurement_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteGroup
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalWasteSubgroup

@Dao
interface WasteGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWasteGroups(groups: List<LocalWasteGroup>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWasteSubroups(subgroups: List<LocalWasteSubgroup>): Completable

    @Query("DELETE FROM ${LocalWasteGroup.TABLE_NAME}")
    fun clearGroups(): Completable

    @Query("DELETE FROM ${LocalWasteSubgroup.TABLE_NAME}")
    fun clearSubgroups(): Completable
}