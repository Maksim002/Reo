package ru.ktsstudio.feature_mno_list.di.list

import ru.ktsstudio.common.domain.filter.FilterProvider
import ru.ktsstudio.common.domain.filter.FilterUpdater

/**
 * @author Maxim Myalkin (MaxMyalkin) on 14.10.2020.
 */
interface MnoListDependencies {
    fun filterProvider(): FilterProvider
    fun filterUpdater(): FilterUpdater
}