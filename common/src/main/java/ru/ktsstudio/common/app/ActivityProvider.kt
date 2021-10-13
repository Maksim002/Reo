package ru.ktsstudio.common.app

import android.app.Activity

interface ActivityProvider {
    fun getCurrentActivity(): Activity?
}
