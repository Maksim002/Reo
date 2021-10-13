package ru.ktsstudio.common.data.error

/**
 * @author Maxim Myalkin (MaxMyalkin) on 31.08.2020.
 */
class CompositeErrorReporter(
    private val reporterList: List<ErrorReporter>
) : ErrorReporter {
    override fun setUserId(userId: String) {
        reporterList.forEach { it.setUserId(userId) }
    }

    override fun logError(throwable: Throwable) {
        reporterList.forEach { it.logError(throwable) }
    }
}
