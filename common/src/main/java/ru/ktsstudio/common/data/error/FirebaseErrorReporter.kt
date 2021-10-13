package ru.ktsstudio.common.data.error

import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Created by Igor Park on 21/07/2020.
 */
class FirebaseErrorReporter : ErrorReporter {
    override fun setUserId(userId: String) {
        FirebaseCrashlytics.getInstance().setUserId(userId)
    }

    override fun logError(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}
