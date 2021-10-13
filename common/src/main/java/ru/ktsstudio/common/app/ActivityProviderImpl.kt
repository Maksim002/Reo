package ru.ktsstudio.common.app

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityProviderImpl : ActivityProvider, Application.ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    override fun getCurrentActivity(): Activity? = currentActivity

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity == activity) currentActivity = null
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {}
}
