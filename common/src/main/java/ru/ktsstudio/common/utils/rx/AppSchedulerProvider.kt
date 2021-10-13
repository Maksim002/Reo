package ru.ktsstudio.common.utils.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class AppSchedulerProvider @Inject constructor(): SchedulerProvider {
    override val io: Scheduler
        get() = Schedulers.io()
    override val computation: Scheduler
        get() = Schedulers.computation()
    override val single: Scheduler
        get() = Schedulers.single()
    override val ui: Scheduler
        get() = AndroidSchedulers.mainThread()
}
