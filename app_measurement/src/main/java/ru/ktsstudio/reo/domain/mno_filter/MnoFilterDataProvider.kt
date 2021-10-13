package ru.ktsstudio.reo.domain.mno_filter

import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.common.domain.filter.FilterKey
import ru.ktsstudio.common.domain.filter.data.FilterDataProvider
import ru.ktsstudio.core_data_measurement_api.data.CategoryRepository
import ru.ktsstudio.core_data_measurement_api.data.model.MnoFilterApplier
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 16.10.2020.
 */
class MnoFilterDataProvider @Inject constructor(
    private val categoryRepository: CategoryRepository
) : FilterDataProvider<MnoFilterItem> {
    override fun observeData(): Observable<Pair<FilterKey, List<MnoFilterItem>>> {
        return categoryRepository.observeMnoCategories()
            .map { MnoFilterApplier.CATEGORY_KEY to it.map(MnoFilterItem::CategoryItem) }
    }
}
