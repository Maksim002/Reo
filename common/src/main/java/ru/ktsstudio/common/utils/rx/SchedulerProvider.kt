package ru.ktsstudio.common.utils.rx
import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {
    val io: Scheduler
    val computation: Scheduler
    val single: Scheduler
    val ui: Scheduler
}
