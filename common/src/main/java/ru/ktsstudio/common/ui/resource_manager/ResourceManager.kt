package ru.ktsstudio.common.ui.resource_manager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes resId: Int): String
    fun getColor(@ColorRes resId: Int): Int
    fun getString(resId: Int, vararg formatArgs: Any?): String
    fun getPluralString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String
    fun getDimensionPixelSize(@DimenRes sizeRes: Int): Int
}
