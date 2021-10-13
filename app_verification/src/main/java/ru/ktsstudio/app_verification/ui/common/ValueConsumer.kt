package ru.ktsstudio.app_verification.ui.common

/**
 * Created by Igor Park on 04/12/2020.
 */
interface ValueConsumer<Value, Updatable> : Updater<Updatable> {
    fun get(): Value
    fun consume(value: Value): Updater<Updatable>
}

interface CheckableValueConsumer<Value, Updatable> : ValueConsumer<Value, Updatable> {
    val isChecked: Boolean
    fun setChecked(isChecked: Boolean): Updater<Updatable>
}

interface Updater<Updatable> {
    fun update(updatable: Updatable): Updatable
}
