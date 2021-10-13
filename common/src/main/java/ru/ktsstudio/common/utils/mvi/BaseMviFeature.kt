package ru.ktsstudio.common.utils.mvi

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import io.reactivex.Observable
import timber.log.Timber

/**
 * Created by Igor Park on 21/07/2020.
 */
open class BaseMviFeature<Wish : Any, in Effect : Any, State : Any, News : Any>(
    initialState: State,
    bootstrapper: Bootstrapper<Wish>? = null,
    actor: Actor<State, Wish, Effect>,
    reducer: Reducer<State, Effect>,
    newsPublisher: NewsPublisher<Wish, Effect, State, News>? = null
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = initialState,
    bootstrapper = bootstrapper,
    actor = actor.errorLoggerWrapper(),
    reducer = reducer,
    newsPublisher = newsPublisher
)


fun <State, Wish, Effect> Actor<State, Wish, Effect>.errorLoggerWrapper(): Actor<State, Wish, Effect> {
    return wrappedActor { observable -> observable.doOnError(Timber::e)
        .doOnNext {
            if(it is ErrorEffect) {
                Timber.e(it.throwable)
            }
        }}
}

fun <State, Wish, Effect> Actor<State, Wish, Effect>.wrappedActor(
    postCompute: (Observable<out Effect>) -> Observable<out Effect>
): Actor<State, Wish, Effect> {
    return ActorFunctionWrapper(
        computable = this,
        postCompute = postCompute
    )
}

