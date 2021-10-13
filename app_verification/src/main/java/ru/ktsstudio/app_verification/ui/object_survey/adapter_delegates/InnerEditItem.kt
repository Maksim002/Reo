package ru.ktsstudio.app_verification.ui.object_survey.adapter_delegates

import ru.ktsstudio.app_verification.ui.common.ValueConsumer
import ru.ktsstudio.common.utils.text_format.TextFormat

data class InnerEditItem(
    val entityId: Any? = null,
    val editHint: String,
    val inputFormat: TextFormat,
    val inCard: Boolean = false,
    val valueConsumer: ValueConsumer<String?, *>,
) {
    val id = "$entityId-$editHint-$inputFormat"
}
