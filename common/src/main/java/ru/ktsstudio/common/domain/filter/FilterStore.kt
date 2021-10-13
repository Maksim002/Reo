package ru.ktsstudio.common.domain.filter

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */
class FilterStore: FilterUpdater, FilterProvider {

    private val currentFilter = BehaviorSubject.createDefault(Filter.EMPTY)

    override fun updateFilter(newFilter: Filter) {
        if (newFilter != currentFilter.value) {
            currentFilter.onNext(newFilter)
        }
    }

    override fun observeFilter(): Observable<Filter> {
        return currentFilter.distinctUntilChanged()
    }
}