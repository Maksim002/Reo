package ru.ktsstudio.common.utils.rx

import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import ru.ktsstudio.common.utils.isNetworkUnavailableException
import java.util.concurrent.TimeUnit

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.03.2020.
 */

fun <T> Single<T>.consumeNetworkUnavailableException(
    action: () -> Single<T>
): Single<T> {
    return onErrorResumeNext { error ->
        if (error.isNetworkUnavailableException()) {
            action()
        } else {
            Single.error(error)
        }
    }
}

fun <T> Maybe<T>.consumeNetworkUnavailableException(
    action: () -> Maybe<T>
): Maybe<T> {
    return onErrorResumeNext { error ->
        if (error.isNetworkUnavailableException()) {
            action()
        } else {
            Maybe.error(error)
        }
    }
}

fun <T> Maybe<T>.errorIfEmpty(): Maybe<T> {
    return toSingle()
        .toMaybe()
}

fun <T> Single<T>.alsoCompletable(
    completable: (T) -> Completable
): Single<T> = flatMap { value ->
    completable(value)
        .andThen(Single.just(value))
}

fun <T> Observable<T>.alsoCompletable(
    completable: (T) -> Completable
): Observable<T> = flatMap { value ->
    completable(value)
        .andThen(Observable.just(value))
}

fun <T> Maybe<T>.alsoCompletable(
    completable: (T) -> Completable
): Maybe<T> = flatMap { value ->
    completable(value)
        .andThen(Maybe.just(value))
}

fun Completable.onErrorCompletableAndConsumeNetworkUnavailable(
    errorMapper: (Throwable) -> Completable
): Completable {
    return onErrorResumeNext { error ->
        errorMapper.invoke(error)
            .andThen(
                if (error.isNetworkUnavailableException()) {
                    Completable.complete()
                } else {
                    Completable.error(error)
                }
            )
    }
}

fun Completable.onErrorCompletableAndThrow(
    errorMapper: (Throwable) -> Completable
): Completable {
    return onErrorResumeNext { error ->
        errorMapper.invoke(error)
            .andThen(Completable.error(error))
    }
}

fun <T> Observable<List<T>>.firstListItem(): Maybe<T> {
    return take(1)
        .single(emptyList())
        .flatMapMaybe { list ->
            list.firstOrNull()?.let { Maybe.just(it) } ?: Maybe.empty()
        }
}

fun <T> Observable<T>.skipFirstIf(predicate: (T) -> Boolean): Observable<T> {
    return publish {
        Observable.merge(
            it.take(1)
                .filter(predicate),
            it.skip(1)
        )
    }
}

fun <T> paginateReduced(
    query: (limit: Int, offset: Int) -> Observable<List<T>>,
    limit: Int = 500
): Single<List<T>> {
    return Observable.range(0, Integer.MAX_VALUE - 1)
        .concatMap {
            query(limit, it * limit)
        }
        .takeUntil { it.size < limit }
        .collectInto(mutableListOf<T>(), { t1, t2 -> t1.addAll(t2) })
        .map { it as List<T> }
}

fun <T, ID> paginateFromReduced(
    query: (limit: Int, from: ID?) -> Observable<List<T>>,
    extractId: T.() -> ID,
    initialFrom: ID? = null,
    limit: Int = 500
): Single<List<T>> {
    var currentFrom = initialFrom
    return Observable.range(0, Integer.MAX_VALUE - 1)
        .concatMap {
            query(limit, currentFrom)
        }
        .doOnNext { currentFrom = it.lastOrNull()?.let { it.extractId() } }
        .takeUntil { it.size < limit }
        .collectInto(mutableListOf<T>(), { t1, t2 -> t1.addAll(t2) })
        .map { it as List<T> }
}

fun <T> Observable<T>.delayFirst(millis: Long): Observable<T> {
    return Observable.combineLatest(
        this,
        Observable.timer(millis, TimeUnit.MILLISECONDS),
        BiFunction { value, _ ->
            value
        }
    )
}

fun <T> io.reactivex.Observable<T>.toRx3Observable(): Observable<T> {
    return RxJavaBridge.toV3Observable<T>(this)
}

fun io.reactivex.Completable.toRx3Completable(): Completable {
    return RxJavaBridge.toV3Completable(this)
}

fun Completable.toRx2Completable(): io.reactivex.Completable {
    return RxJavaBridge.toV2Completable(this)
}

fun <T> io.reactivex.Single<T>.toRx3Single(): Single<T> {
    return RxJavaBridge.toV3Single(this)
}

fun <T> Single<T>.toRx2Single(): io.reactivex.Single<T> {
    return RxJavaBridge.toV2Single(this)
}

fun <T> io.reactivex.Maybe<T>.toRx3Maybe(): Maybe<T> {
    return RxJavaBridge.toV3Maybe(this)
}

fun <T> Observable<T>.toRx2Observable(): io.reactivex.Observable<T> {
    return RxJavaBridge.toV2Observable(this)
}

typealias Rx2PublishSubject<T> = io.reactivex.subjects.PublishSubject<T>
typealias Rx2Observable<T> = io.reactivex.Observable<T>
typealias Rx3Observable<T> = Observable<T>
typealias Rx3Maybe<T> = Maybe<T>
typealias Rx3Completable = Completable
typealias Rx3Single<T> = Single<T>
