package ru.ktsstudio.common.presentation.filter

import ru.ktsstudio.common.domain.filter.FilterKey

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.10.2020.
 */
interface FilterUiFieldToKeyMapper<Field> {
    fun map(from: Field): FilterKey
}