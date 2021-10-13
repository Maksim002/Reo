package ru.ktsstudio.common.domain.filter

import io.reactivex.rxjava3.core.Observable

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */
interface FilterProvider {
    fun observeFilter(): Observable<Filter>
}