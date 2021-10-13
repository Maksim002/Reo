package ru.ktsstudio.feature_sync_impl.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import io.reactivex.rxjava3.disposables.Disposable
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.feature_sync_api.domain.SyncInteractor
import ru.ktsstudio.feature_sync_impl.R
import ru.ktsstudio.feature_sync_impl.di.SyncFeatureComponent
import timber.log.Timber
import javax.inject.Inject

class SyncService : Service() {

    @Inject
    lateinit var syncInteractor: SyncInteractor

    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        goForeground()
        inject()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (disposable?.isDisposed != false) {
            syncInteractor.sync()
                .doFinally(::stopSelf)
                .subscribe(
                    { },
                    { Timber.e(it) }
                )
                .let { disposable = it }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun inject() {
        ComponentRegistry.get<SyncFeatureComponent>()
            .serviceComponent()
            .inject(this)
    }

    private fun goForeground() {
        createNotificationChannel()

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_sync_title))
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_sync_name),
                NotificationManager.IMPORTANCE_LOW
            )

            ContextCompat.getSystemService(this, NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "Channel"
        private const val NOTIFICATION_ID = 1
    }
}