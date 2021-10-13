package ru.ktsstudio.common.presentation

import android.widget.Toast

/**
 * @author Maxim Myalkin (MaxMyalkin) on 12.03.2021.
 */
data class ToastMessage(
    val message: String?,
    val length: Int
)

fun String?.toToastMessage(length: Int = Toast.LENGTH_SHORT): ToastMessage {
    return ToastMessage(this.orEmpty(), length)
}
