package ru.ktsstudio.common.utils.mvi

import io.reactivex.Observable

/**
 * Created by Igor Park on 21/07/2020.
 */
class ActorFunctionWrapper<State, Action, Effect>(
    private val computable: Function2<State, Action, Observable<out Effect>>,
    private val postCompute: (Observable<out Effect>) -> Observable<out Effect>
) : Function2<State, Action, Observable<out Effect>> {
    override fun invoke(state: State, action: Action): Observable<out Effect> {
        val typedValue = computable.invoke(state, action)
        return postCompute.invoke(typedValue)
    }
}
