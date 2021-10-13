package ru.ktsstudio.common.domain.filter

/**
 * @author Maxim Myalkin (MaxMyalkin) on 13.10.2020.
 */

typealias FilterKey = String

data class Filter(
    val searchQuery: String,
    val filterMap: Map<FilterKey, Any>
) {
    companion object {
        val EMPTY = Filter("", emptyMap())
    }
}