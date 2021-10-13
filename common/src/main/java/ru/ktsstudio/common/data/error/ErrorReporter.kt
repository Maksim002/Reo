package ru.ktsstudio.common.data.error

/**
 * Created by Igor Park on 21/07/2020.
 */
interface ErrorReporter {
    fun setUserId(userId: String)
    fun logError(throwable: Throwable)
}
