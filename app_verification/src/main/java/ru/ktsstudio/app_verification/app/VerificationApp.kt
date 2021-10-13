package ru.ktsstudio.app_verification.app

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.jakewharton.threetenabp.AndroidThreeTen
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import ru.ktsstudio.app_verification.BuildConfig
import ru.ktsstudio.app_verification.di.app.VerificationAppComponent
import ru.ktsstudio.common.app.AppCodeProvider
import ru.ktsstudio.common.utils.log.ErrorReportingTree
import ru.ktsstudio.common.utils.rx.RxErrorHandler
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_network_api.CoreNetworkApi
import timber.log.Timber
import java.util.UUID

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.09.2020.
 */
class VerificationApp : Application(), AppCodeProvider {
    private var appCodeString: String = ""

    override val appCode: String
        get() = appCodeString

    override fun onCreate() {
        super.onCreate()
        appCodeString = UUID.randomUUID().toString()
        initThreeten()
        initDi()
        initTimber()
        initRxErrorHandler()
        initFlipper()
        initActivityProvider()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        val errorReporter = ComponentRegistry.get<VerificationAppComponent>().errorReporter()
        Timber.plant(ErrorReportingTree(errorReporter))
    }

    private fun initRxErrorHandler() {
        RxJavaPlugins.setErrorHandler(RxErrorHandler())
    }

    private fun initFlipper() {
        val shouldInitFlipper = BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)
        if (!shouldInitFlipper) return

        SoLoader.init(this, false)

        val context = this
        AndroidFlipperClient.getInstance(context).apply {
            addPlugin(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))
            addPlugin(DatabasesFlipperPlugin(context))
            addPlugin(SharedPreferencesFlipperPlugin(context, "reo_prefs"))
            addPlugin(ComponentRegistry.get<CoreNetworkApi>().flipperNetworkPlugin())
        }
            .start()
    }

    private fun initDi() {
        ComponentInitializer(this).initialize()
    }

    private fun initActivityProvider() {
        registerActivityLifecycleCallbacks(
            ComponentRegistry.get<VerificationAppComponent>().activityLifecycleCallbacks()
        )
    }

    private fun initThreeten() {
        AndroidThreeTen.init(this)
    }
}
