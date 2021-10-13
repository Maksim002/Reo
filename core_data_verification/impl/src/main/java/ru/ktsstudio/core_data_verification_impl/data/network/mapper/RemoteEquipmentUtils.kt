package ru.ktsstudio.core_data_verification_impl.data.network.mapper

/**
 * @author Maxim Myalkin (MaxMyalkin) on 21.12.2020.
 */

fun <T> Iterable<T>.getIndexes(predicate: (T) -> Boolean = { true }): List<Int> {
    val indexList = mutableListOf<Int>()
    forEachIndexed { index, t ->
        if (predicate(t)) {
            indexList.add(index)
        }
    }
    return indexList
}