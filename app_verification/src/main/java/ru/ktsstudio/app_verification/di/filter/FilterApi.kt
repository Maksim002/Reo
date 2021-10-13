package ru.ktsstudio.app_verification.di.filter

import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.domain.filter.FilterUpdater

/**
 * @author Maxim Myalkin (MaxMyalkin) on 16.11.2020.
 */
interface FilterApi {
    fun objectFilterProvider(): FilterProvider
    fun objectFilterUpdater(): FilterUpdater
}
