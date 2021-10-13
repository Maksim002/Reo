package ru.ktsstudio.common.utils.db

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Created by Igor Park on 03/11/2020.
 */
private const val BATCH_SIZE = 999

fun <T> batchedCompletable(
    list: List<T>,
    batchSize: Int = BATCH_SIZE,
    query: (List<T>) -> Completable
): Completable {
    return Completable.fromCallable {
        list.chunked(batchSize).forEach { batch ->
            query.invoke(batch).blockingAwait()
        }
    }
}

fun <T, R> List<T>.batchedObserve(query: (list: List<T>) -> Observable<List<R>>): Observable<List<R>> {
    return if (isEmpty()) {
        Observable.just(emptyList())
    } else {
        chunked(BATCH_SIZE)
            .map { query(it) }
            .let { Observable.zip(it) { array: Array<Any> -> array.flatMap { it as List<R> } } }
    }
}

fun <T, R> List<T>.batchedQuerySingle(query: (list: List<T>) -> Single<List<R>>): Single<List<R>> {
    return if (isEmpty()) {
        Single.just(emptyList())
    } else {
        chunked(BATCH_SIZE)
            .map { query(it) }
            .let { Single.zip(it) { array: Array<Any> -> array.flatMap { it as List<R> } } }
    }
}