package ru.ktsstudio.common.ui.view_model
import androidx.annotation.CallSuper
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo

abstract class RxViewModel : DetachableViewModel() {

    private val subscriptions = CompositeDisposable()
    private val foregroundSubscriptions = CompositeDisposable()

    @CallSuper
    override fun onDetach() {
        foregroundSubscriptions.clear()
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    protected fun Disposable.store() = addTo(subscriptions)
    protected fun Disposable.storeForeground() = addTo(foregroundSubscriptions)
}
