package ru.ktsstudio.common.domain.filter

import io.reactivex.Observable

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.10.2020.
 */

fun <Effect> doIfFilterChange(
    newFilter: Filter,
    oldFilter: Filter,
    updateFilterEffect: Effect,
    action: () -> Observable<Effect>
): Observable<Effect> {
    return if (newFilter == oldFilter) {
        Observable.empty<Effect>()
    } else {
        action().startWith(updateFilterEffect)
    }
}