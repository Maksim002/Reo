package ru.ktsstudio.common.utils

import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 02.11.2020.
 */
class ContainsTextChecker @Inject constructor() {

    private val multipleSpacesRegex = "\\s+".toRegex()
    private val yoRegex = "ё".toRegex()

    fun contains(query: String, original: String): Boolean {
        val preparedQuery = query.trim()
            .trimMultipleSpaces()
            .replaceYo()
        val preparedOriginal = original.trimMultipleSpaces().replaceYo()
        return preparedOriginal.contains(preparedQuery, ignoreCase = true)
    }

    private fun String.trimMultipleSpaces(): String = replace(multipleSpacesRegex, " ")
    private fun String.replaceYo(): String = replace(yoRegex, "е")

}