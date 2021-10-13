package ru.ktsstudio.common.utils.log

import android.util.Log
import ru.ktsstudio.common.data.error.ErrorReporter
import timber.log.Timber

class ErrorReportingTree(
    private val errorReporter: ErrorReporter
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < Log.ERROR || t == null) return
        errorReporter.logError(t)
    }
}
