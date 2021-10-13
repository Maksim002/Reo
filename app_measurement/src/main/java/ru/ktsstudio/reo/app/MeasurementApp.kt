package ru.ktsstudio.reo.app

import android.app.Application
import com.badoo.mvicore.consumer.middleware.LoggingMiddleware
import com.badoo.mvicore.consumer.middlewareconfig.MiddlewareConfiguration
import com.badoo.mvicore.consumer.middlewareconfig.Middlewares
import com.badoo.mvicore.consumer.middlewareconfig.WrappingCondition
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.jakewharton.threetenabp.AndroidThreeTen
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import ru.ktsstudio.common.app.AppCodeProvider
import ru.ktsstudio.common.utils.log.ErrorReportingTree
import ru.ktsstudio.common.utils.rx.RxErrorHandler
import ru.ktsstudio.common_registry.ComponentRegistry
import ru.ktsstudio.core_network_api.CoreNetworkApi
import ru.ktsstudio.reo.BuildConfig
import ru.ktsstudio.reo.di.app.MeasurementAppComponent
import timber.log.Timber
import java.util.UUID

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.09.2020.
 */
class MeasurementApp : Application(), AppCodeProvider {

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
        initMviCoreLogging()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        val errorReporter = ComponentRegistry.get<MeasurementAppComponent>().errorReporter()
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
            ComponentRegistry.get<MeasurementAppComponent>().activityLifecycleCallbacks()
        )
    }

    private fun initThreeten() {
        AndroidThreeTen.init(this)
    }

    private fun initMviCoreLogging() {
        Middlewares.configurations.add(
            MiddlewareConfiguration(
                condition = WrappingCondition.Always,
                factories = listOf { consumer -> LoggingMiddleware(consumer, { Timber.tag("MVICore").v(it) }) }
            )
        )
    }
}
