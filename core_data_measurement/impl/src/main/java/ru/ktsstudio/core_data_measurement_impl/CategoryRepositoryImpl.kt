package ru.ktsstudio.core_data_measurement_impl

import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.core_data_measurement_api.data.CategoryRepository
import ru.ktsstudio.core_data_measurement_api.data.model.Category
import ru.ktsstudio.core_data_measurement_impl.data.db.MnoStore
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 16.10.2020.
 */
class CategoryRepositoryImpl @Inject constructor(
    private val mnoStore: MnoStore,
    private val schedulers: SchedulerProvider
) : CategoryRepository {
    override fun observeMnoCategories(): Observable<List<Category>> {
        return mnoStore.observeAllMno()
            .map {
                it.map {
                    Category(id = it.category.id, name = it.category.name)
                }.distinctBy { it.id }
            }
            .subscribeOn(schedulers.io)
    }
}