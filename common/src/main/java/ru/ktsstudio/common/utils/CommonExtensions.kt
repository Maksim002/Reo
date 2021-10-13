package ru.ktsstudio.common.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.ktsstudio.common.data.network.NetworkUnavailableException
import ru.ktsstudio.utilities.file.FileUtils
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Locale

/**
 * @author Maxim Myalkin (MaxMyalkin) on 22.09.2020.
 */

fun Throwable.isNetworkUnavailableException() = when (this) {
    is UnknownHostException,
    is SocketTimeoutException,
    is IOException,
    is NetworkUnavailableException -> true
    else -> false
}

fun Any.isClassEqualTo(other: Any): Boolean {
    return this::class.qualifiedName == other::class.qualifiedName
}

fun Context.resourcesLocale(locale: Locale? = Locale("ru")): Resources {
    val configuration = Configuration(resources.configuration).apply {
        setLocale(locale)
    }
    return createConfigurationContext(configuration).resources
}

val Any?.exhaustive get() = Unit

fun Float?.stringValue(): String {
    if (this == null) return ""
    return if (this % 1.0F != 0F) {
        String.format("%s", this)
    } else {
        String.format("%.0f", this)
    }
}

fun String.floatValue(): Float? {
    return takeIf { isNotBlank() }?.toFloatOrNull()
}

fun <T> checkValue(value: T?, parameterName: String): T {
    return value ?: error("Parameter: $parameterName is null")
}

fun Intent.checkAvailableReceiver(packageManager: PackageManager): Intent? {
    return this.takeIf {
        resolveActivity(packageManager) != null
    }
}

fun File.toFormData(formDataName: String = FORM_DATA_NAME): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        formDataName,
        name,
        RequestBody.create(
            MediaType.parse(FileUtils.getMimeType(this)),
            this
        )
    )
}

private const val FORM_DATA_NAME = "file"

fun String.setRequiredSign(isRequired: Boolean): String {
    return this + "*".takeIf { isRequired }.orEmpty()
}

fun Float?.orDefault(default: Float) = this ?: default

fun <T> Collection<T>.toggleItem(item: T): Set<T> {
    return this.toMutableSet()
        .apply {
            if (contains(item)) {
                remove(item)
            } else {
                add(item)
            }
        }
}

fun <T> Collection<T>.replace(item: T, predicate: (T) -> Boolean): Set<T> {
    return toMutableSet()
        .apply {
            removeAll(filter(predicate))
            add(item)
        }
}

fun <T, K> mergeItems(
    higherPriorityList: List<T>,
    lowerPriorityList: List<T>,
    keySelector: (T) -> K
): List<T> {
    return (higherPriorityList + lowerPriorityList)
        .groupBy { keySelector(it) }
        .filterValues { it.size <= 2 }
        .mapValues { it.value.first() }
        .values
        .toList()
}
