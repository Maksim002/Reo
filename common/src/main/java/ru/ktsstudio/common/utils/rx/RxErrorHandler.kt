package ru.ktsstudio.common.utils.rx
import android.database.SQLException
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.functions.Consumer
import ru.ktsstudio.common.data.network.NetworkUnavailableException
import ru.ktsstudio.common.data.network.RetrofitException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RxErrorHandler : Consumer<Throwable> {

    override fun accept(t: Throwable?) {
        if (t == null) return
        if (isApplicable(t)) {
            swallow(t)
            return
        }

        Timber.e(t)
        uncaught(t)
    }

    private fun isApplicable(t: Throwable): Boolean {
        return t is UndeliverableException && (t.cause is NetworkUnavailableException ||
            t.cause is RetrofitException ||
            t.cause is UnknownHostException ||
            t.cause is SocketTimeoutException ||
            t.cause is IOException ||
            t.cause is SQLException ||
            t.cause is java.sql.SQLException)
    }

    private fun swallow(error: Throwable) = Timber.w(error)

    private fun uncaught(error: Throwable) {
        val currentThread = Thread.currentThread()
        val handler = currentThread.uncaughtExceptionHandler

        handler?.uncaughtException(currentThread, error)
    }
}
