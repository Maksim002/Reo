package ru.ktsstudio.feature_mno_list.navigation

/**
 * @author Maxim Myalkin (MaxMyalkin) on 02.10.2020.
 */
interface MnoNavigator {
    fun openMnoDetails(id: String)
    fun openMnoFilter()
    fun startMeasurement(mnoId: String)
}