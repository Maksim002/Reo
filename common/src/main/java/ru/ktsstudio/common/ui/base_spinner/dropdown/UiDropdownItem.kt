package ru.ktsstudio.common.ui.base_spinner.dropdown

interface UiDropdownItem<T> {
    val id: T
    val title: String
    val isSelected: Boolean
}