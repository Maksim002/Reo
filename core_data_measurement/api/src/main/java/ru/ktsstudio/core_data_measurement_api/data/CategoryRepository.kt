package ru.ktsstudio.core_data_measurement_api.data

import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.core_data_measurement_api.data.model.Category

/**
 * @author Maxim Myalkin (MaxMyalkin) on 16.10.2020.
 */
interface CategoryRepository {
    fun observeMnoCategories(): Observable<List<Category>>
}