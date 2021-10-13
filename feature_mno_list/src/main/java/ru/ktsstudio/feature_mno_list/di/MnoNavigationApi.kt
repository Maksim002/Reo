package ru.ktsstudio.feature_mno_list.di

import ru.ktsstudio.feature_mno_list.navigation.MnoNavigator

/**
 * @author Maxim Myalkin (MaxMyalkin) on 02.10.2020.
 */
interface MnoNavigationApi {
    fun mnoNavigation(): MnoNavigator
}