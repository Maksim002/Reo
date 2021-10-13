package ru.ktsstudio.reo.domain.mno_filter

import ru.ktsstudio.core_data_measurement_api.data.model.Category

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
sealed class MnoFilterItem {

    abstract val id: String

    data class CategoryItem(
        val category: Category
    ) : MnoFilterItem() {
        override val id: String
            get() = category.id
    }
}
