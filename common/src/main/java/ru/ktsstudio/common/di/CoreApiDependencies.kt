package ru.ktsstudio.common.di

import android.content.Context
import ru.ktsstudio.common.app.AppCodeProvider
import ru.ktsstudio.common.data.AppVersion

/**
 * @author Maxim Myalkin (MaxMyalkin) on 23.09.2020.
 */
interface CoreApiDependencies {
    fun context(): Context
    fun appCodeProvider(): AppCodeProvider
    @AppVersion fun appVersion(): String
}