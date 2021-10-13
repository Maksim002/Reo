package ru.ktsstudio.app_verification.presentation.object_survey.common

import io.reactivex.ObservableSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.ktsstudio.common.utils.rx.Rx2Observable
import ru.ktsstudio.common.utils.rx.toRx2Observable
import ru.ktsstudio.common.utils.rx.toRx3Observable
import java.util.concurrent.TimeUnit

/**
 * @author Maxim Myalkin (MaxMyalkin) on 18.02.2021.
 */

private const val INPUT_DEBOUNCE = 200L

fun <T> Rx2Observable<T>.formDebounce(): Rx2Observable<T> {
    return debounce(INPUT_DEBOUNCE, TimeUnit.MILLISECONDS)
        .toRx3Observable()
        .observeOn(AndroidSchedulers.mainThread())
        .toRx2Observable()
}

fun <T> ObservableSource<T>.distinctUntilChanged(): Rx2Observable<T> {
    return Rx2Observable.wrap(this).distinctUntilChanged()
}
