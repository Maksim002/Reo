package ru.ktsstudio.common.domain.filter.data

import io.reactivex.rxjava3.core.Observable
import ru.ktsstudio.common.domain.filter.FilterKey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
interface FilterDataProvider<DataType> {
    fun observeData(): Observable<Pair<FilterKey, List<DataType>>>
}