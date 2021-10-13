package ru.ktsstudio.common.domain.filter.data

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.ktsstudio.common.utils.rx.SchedulerProvider
import ru.ktsstudio.common.utils.rx.toRx2Observable

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
class FilterDataActor<DataType>(
    private val filterDataProvider: FilterDataProvider<DataType>,
    private val schedulers: SchedulerProvider
) : Actor<
    FilterDataFeature.State<DataType>,
    FilterDataFeature.Wish,
    FilterDataFeature.Effect<DataType>
    > {
    override fun invoke(
        state: FilterDataFeature.State<DataType>,
        action: FilterDataFeature.Wish
    ): Observable<out FilterDataFeature.Effect<DataType>> {
        return when (action) {
            FilterDataFeature.Wish.Load -> filterDataProvider
                .observeData()
                .map { (filterKey, data) -> FilterDataFeature.Effect.ChangeData(filterKey, data) }
                .onErrorComplete()
                .observeOn(schedulers.ui)
                .toRx2Observable()
        }
    }
}