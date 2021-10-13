package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.CheckableValueConsumer
import ru.ktsstudio.common.utils.text_format.TextFormat

data class LabeledMultilineItemWithCheck(
    val valueConsumer: CheckableValueConsumer<String?, *>,
    val label: String,
    val editHint: String,
    val inputFormat: TextFormat
)
