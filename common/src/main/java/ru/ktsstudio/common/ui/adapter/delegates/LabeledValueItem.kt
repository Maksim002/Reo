package ru.ktsstudio.common.ui.adapter.delegates

/**
 * @author Maxim Myalkin (MaxMyalkin) on 05.10.2020.
 */
data class LabeledValueItem(
    val label: String,
    val value: String,
    val inCard: Boolean = false
)