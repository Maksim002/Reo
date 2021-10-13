package ru.ktsstudio.common.ui.adapter.delegates

/**
 * @author Maxim Ovchinnikov on 14.10.2020.
 */
data class DoubleLabeledValueItem(
    val leftLabel: String,
    val leftValue: String,
    val rightLabel: String,
    val rightValue: String,
    val inCard: Boolean = false
)