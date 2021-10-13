package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.common.utils.text_format.TextFormat

/**
 * @author Maxim Ovchinnikov on 27.11.2020.
 */
data class LabeledEditItemWithCheck(
    val checkableValueConsumer: CheckableValueConsumer<String?, *>,
    val label: String,
    val editHint: String,
    val inputFormat: TextFormat,
    val enabled: Boolean = true
)
