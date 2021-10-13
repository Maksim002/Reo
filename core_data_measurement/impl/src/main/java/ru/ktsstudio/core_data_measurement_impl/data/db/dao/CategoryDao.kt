package ru.ktsstudio.core_data_measurement_impl.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.rxjava3.core.Completable
import ru.ktsstudio.core_data_measurement_impl.data.db.model.LocalCategory

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.10.2020.
 */
@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(categories: List<LocalCategory>): Completable


}