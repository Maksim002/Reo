package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.text_format.TextFormat

data class LabeledEditDeletableItem<EntityQualifier>(
    val id: String,
    val qualifier: EntityQualifier,
    val label: String,
    val editHint: String,
    val inputFormat: TextFormat,
    val valueConsumer: ValueConsumer<String?, *>
)
