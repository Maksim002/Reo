package ru.ktsstudio.common.utils.gson

import com.google.gson.Gson
import ru.ktsstudio.utilities.extensions.fromJson
import ru.ktsstudio.utilities.extensions.requireNotNull

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.12.2020.
 */

inline fun <reified T: Enum<T>> Gson.deserializeEnum(string: String): T {
    return fromJson<T>(string).requireNotNull()
}