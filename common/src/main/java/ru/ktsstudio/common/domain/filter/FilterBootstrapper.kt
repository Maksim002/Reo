package ru.ktsstudio.common.domain.filter

import com.badoo.mvicore.element.Bootstrapper
import io.reactivex.Observable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class FilterBootstrapper(
    private val filterProvider: FilterProvider,
    private val schedulerProvider: SchedulerProvider
): Bootstrapper<FilterFeature.Wish> {
    override fun invoke(): Observable<FilterFeature.Wish> {
        return filterProvider.observeFilter()
            .observeOn(schedulerProvider.ui)
            .map<FilterFeature.Wish> { FilterFeature.Wish.ChangeFilterExternal(it) }
            .toRx2Observable()
    }
}