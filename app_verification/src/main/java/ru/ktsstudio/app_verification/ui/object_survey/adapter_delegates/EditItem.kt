package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.text_format.TextFormat

data class EditItem(
    val entityId: Any? = null,
    val valueConsumer: ValueConsumer<String?, *>,
    val label: String? = null,
    val editHint: String,
    val inputFormat: TextFormat,
    val isEditable: Boolean = true
) {
    val id = "$entityId-$label-$editHint-$inputFormat"
}
