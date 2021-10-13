package ru.ktsstudio.common.data.account

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.09.2020.
 */
interface AuthDelegate {
    fun onUnauthorized()
}