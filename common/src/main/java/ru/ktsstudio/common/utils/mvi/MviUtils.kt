package ru.ktsstudio.common.utils.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ru.ktsstudio.common.utils.rx.Rx3Observable

/**
 * @author Maxim Myalkin (MaxMyalkin) on 25.09.2020.
 */

fun <T1, T2> combineLatest(
    o1: ObservableSource<T1>,
    o2: ObservableSource<T2>
): Observable<Pair<T1, T2>> =
    Observable.combineLatest(
        o1,
        o2,
        { t1, t2 -> t1 to t2 }
    )

fun <T> ((T) -> Unit).toConsumer(): Consumer<T> {
    return Consumer {
        this.invoke(it)
    }
}

fun <Result, Action> refreshList(
    createFetchAction: () -> Single<Result>,
    createLoadingAction: () -> Action,
    createSuccessAction: (Result) -> Action,
    createErrorAction: (throwable: Throwable) -> Action
): Rx3Observable<Action> {
    return createFetchAction().toObservable()
        .map { result ->
            createSuccessAction(result)
        }
        .startWithItem(createLoadingAction())
        .onErrorReturn { createErrorAction(it) }
}

fun <T> liveDataConsumer(livedata: MutableLiveData<T>): Consumer<T> = Consumer<T> { t -> livedata.postValue(t) }

fun <T> emptyConsumer(): Consumer<T> = Consumer<T> {}