package ru.ktsstudio.common.utils.mvi

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.09.2020.
 */
interface ErrorEffect {
    val throwable: Throwable
}